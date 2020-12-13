import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import Swal from 'sweetalert2';
import {CookieService} from 'ngx-cookie-service';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';

interface Product {
  id: Number;
  name: String;
  description: String;
  category: number;
  photo: String;
  price: number;
  quantity: number;
}

interface Category {
  id: Number;
  name: String;
}

@Component({
  selector: 'app-manage-products',
  templateUrl: './manage-products.component.html',
  styleUrls: ['./manage-products.component.css']
})
export class ManageProductsComponent implements OnInit {
  status = 'empty';
  productName: any;
  productDescription: any;
  productCategory: any;
  productPhoto: any;
  productPrice: any;
  productQuantity: any;
  products: Product[] = [];
  categories: Category[] = [];
  private token: string;

  constructor(private http: HttpClient, private cookieService: CookieService, private router: Router) {
  }

  ngOnInit() {
    this.token = this.cookieService.get('token');
    const type = this.cookieService.get('type');
    if (type === 'client') {
      this.router.navigate(['products']);
    } else if (type === 'admin') {
    } else {
      this.router.navigate(['login']);
    }
    const headers = {'Authorization': this.token};
    this.http.get<any>(environment.apiEndpoint + '/products', {headers}).subscribe(data => {
      this.products = data;
    });
    this.http.get<any>(environment.apiEndpoint + '/categories', {headers}).subscribe(data => {
      this.categories = data;
    });
  }

  getCategory(id: any) {
    const cat = this.categories.filter(x => x.id === id)[0];
    return cat.name;
  }

  refreshProductsList() {
    const headers = {'Authorization': this.token};
    this.http.get<any>(environment.apiEndpoint + '/products', {headers}).subscribe(data => {
      this.products = data;
    });
  }

  addProduct() {
    console.log(this.productName);
    if (this.productName === undefined || this.productDescription === undefined
      || this.productPhoto === undefined || this.productPrice === undefined
      || this.productQuantity === undefined || this.productCategory === undefined) {
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: 'The fields can\'t be empty !',
        showConfirmButton: false,
        timer: 1500
      });
    } else {
      const headers = {'Authorization': this.token};
      const body = {
        name: this.productName,
        description: this.productDescription,
        photo: this.productPhoto,
        category: this.productCategory,
        quantity: this.productQuantity,
        price: this.productPrice
      };
      this.http.post<any>(environment.apiEndpoint + '/products', body, {headers}).subscribe(data => {
        this.status = data.status;
        if (this.status === '1') {
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'The product has been added successfully !',
            showConfirmButton: false,
            timer: 1500
          });
        }
        this.refreshProductsList();
      });
    }
  }

  deleteProduct(id: any) {
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
        this.http.delete<any>(environment.apiEndpoint + '/products/' + id, {headers}).subscribe(data => {
          this.status = data.status;
          if (this.status === '1') {
            Swal.fire({
              position: 'center',
              icon: 'success',
              title: 'The product has been deleted successfully !',
              showConfirmButton: false,
              timer: 1500
            });
          }
          this.refreshProductsList();
        });
      }
    });
  }

  viewProductPhoto(id: any) {
    const headers = {'Authorization': this.token};
    this.http.get<any>(environment.apiEndpoint + '/products/' + id, {headers}).subscribe(data => {
      if (data.photo !== '') {
        Swal.fire({
          title: data.name,
          text: data.description,
          imageUrl: environment.apiEndpoint + '/products/' + data.photo,
          imageWidth: 400,
          imageHeight: 400,
          imageAlt: 'Custom image',
        });
      }
      // this.refreshProductsList();
    });
  }

  handleUpload(event) {
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      this.productPhoto = reader.result;
    };
  }
}
