import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import Swal from 'sweetalert2';
import {CookieService} from 'ngx-cookie-service';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';

interface CartItem {
  id: Number;
  item: Number;
  quantity: number;
  cart: Number;
}

interface Product {
  id: any;
  item: any;
  name: any;
  description: any;
  photo: any;
  price: any;
  quantity: any;
  total: any;
}

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  status = 'empty';
  items: CartItem[] = [];
  products: Product[] = [];
  finalTotal = 0;
  private token: string;

  constructor(private http: HttpClient, private cookieService: CookieService, private router: Router) {
  }

  ngOnInit() {
    this.token = this.cookieService.get('token');
    const type = this.cookieService.get('type');
    if (type === 'client') {
      // this.router.navigate(['products']);
    } else if (type === 'admin') {
    } else {
      this.router.navigate(['login']);
    }
    this.refreshProductsList();
  }

  refreshProductsList() {
    const id = this.cookieService.get('id');
    const token = this.cookieService.get('token');
    const headers = {'Authorization': token};
    this.http.get<any>(environment.apiEndpoint + '/carts/' + id, {headers}).subscribe(data => {
      this.items = data;
      console.log(data);
      this.items.forEach(item => {
        console.log(item);
        this.http.get<any>(environment.apiEndpoint + '/products/' + item.item, {headers}).subscribe(theData => {
          console.log(theData);
          const element: Product = {
            id: item.id,
            item: item.item,
            name: theData.name,
            description: theData.description,
            price: theData.price,
            photo: theData.photo,
            quantity: item.quantity,
            total: item.quantity * theData.price,
          };
          this.products.push(element);
          this.calculateFinalTotal();
        });
      });
    });
  }

  calculateFinalTotal() {
    this.finalTotal = 0;
    this.products.forEach(element => {
      this.finalTotal += element.quantity * element.price;
    });
  }

  deleteProduct(product) {
    Swal.fire({
      title: 'Are you sure ?',
      text: 'You won\'t be able to undo this!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes'
    }).then((result) => {
      if (result.isConfirmed) {
        const headers = {'Authorization': this.token};
        this.http.delete<any>(environment.apiEndpoint + '/carts/' + product.item, {headers}).subscribe(data => {
          this.status = data.status;
          if (this.status === '1') {
            Swal.fire({
              position: 'center',
              icon: 'success',
              title: 'The product has been deleted successfully from your cart !',
              showConfirmButton: false,
              timer: 1500
            });
          }
        });
      }
    });
  }

  incrementQuantity(product) {
    if (product.quantity < 10) {
      const headers = {'Authorization': this.token};
      this.http.get<any>(environment.apiEndpoint + '/carts/' + product.item + '/increment', {headers}).subscribe(data => {
        this.status = data.status;
        if (this.status === '1') {
          product.quantity += 1;
          product.total = product.price * product.quantity;
          this.calculateFinalTotal();
        }
      });
    }
  }

  decrementQuantity(product) {
    if (product.quantity > 1) {
      const headers = {'Authorization': this.token};
      this.http.get<any>(environment.apiEndpoint + '/carts/' + product.item + '/decrement', {headers}).subscribe(data => {
        this.status = data.status;
        if (this.status === '1') {
          product.quantity -= 1;
          product.total = product.price * product.quantity;
          this.calculateFinalTotal();
        }
      });
    }
  }

  viewProductPhoto(product: any) {
    Swal.fire({
      title: product.name,
      text: product.description,
      imageUrl: environment.apiEndpoint + '/products/' + product.photo,
      imageWidth: 400,
      imageHeight: 400,
      imageAlt: 'Custom image',
    });
  }

  async checkOut() {
    const {value: text} = await Swal.fire({
      title: 'Your shipping address',
      input: 'text',
      inputLabel: 'Enter your Your shipping address',
      inputPlaceholder: ''
    });
    if (text) {
      const id = this.cookieService.get('id');
      const headers = {'Authorization': this.token};
      const body = {id: id, address: text};
      this.http.post<any>(environment.apiEndpoint + '/carts/checkout', body, {headers}).subscribe(data => {
        this.status = data.status;
        console.log(this.status);
        if (this.status === '1') {
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'The operation has been completed successfully !',
            showConfirmButton: false,
            timer: 1500
          });
        } else {
          Swal.fire({
            position: 'center',
            icon: 'error',
            title: 'The given information are wrong !',
            showConfirmButton: false,
            timer: 1500
          });
        }
      });
    }
  }
}
