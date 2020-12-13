import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import Swal from 'sweetalert2';
import {CookieService} from 'ngx-cookie-service';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';

interface Category {
  id: Number;
  name: String;
}

interface Product {
  id: Number;
  name: String;
  description: String;
  category: number;
  photo: String;
  price: number;
  quantity: number;
}

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
  status = 'empty';
  query: any;
  products: Product[] = [];
  categories: Category[] = [];
  userId: any;
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
    this.userId = this.cookieService.get('id');
    const headers = {'Authorization': this.token};
    this.http.get<any>(environment.apiEndpoint + '/products', {headers}).subscribe(data => {
      this.products = data;
    });
    this.http.get<any>(environment.apiEndpoint + '/categories', {headers}).subscribe(data => {
      this.categories = data;
    });
  }

  search() {
    if (this.query === undefined || this.query === '') {
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: 'The fields can\'t be empty !',
        showConfirmButton: false,
        timer: 1500
      });
    } else {
      const headers = {'Authorization': this.token};
      this.http.get<any>(environment.apiEndpoint + '/products/search/' + this.query, {headers}).subscribe(data => {
        this.products = data;
        if (this.products.length < 1) {
          Swal.fire({
            position: 'center',
            icon: 'info',
            title: 'There is no results !',
            showConfirmButton: false,
            timer: 1500
          });
        }
      });
    }
  }

  refreshCategoriesList() {
    const headers = {'Authorization': this.token};
    this.http.get<any>(environment.apiEndpoint + '/categories', {headers}).subscribe(data => {
      this.categories = data;
    });
  }

  showAll() {
    this.query = '';
    const headers = {'Authorization': this.token};
    this.http.get<any>(environment.apiEndpoint + '/products', {headers}).subscribe(data => {
      this.products = data;
    });
  }

  addToCart(id: any) {
    const headers = {'Authorization': this.token};
    const body = {item: id, quantity: 1, id: this.userId};
    this.http.post<any>(environment.apiEndpoint + '/carts', body, {headers}).subscribe(data => {
      this.status = data.status;
      if (this.status === '1') {
        Swal.fire({
          position: 'center',
          icon: 'success',
          title: 'This item has been added successfully to your cart !',
          showConfirmButton: false,
          timer: 1500
        });
      }
      this.refreshCategoriesList();
    });
  }

  deleteCategory(id: any) {
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
        this.http.delete<any>(environment.apiEndpoint + '/categories/' + id, {headers}).subscribe(data => {
          this.status = data.status;
          if (this.status === '1') {
            Swal.fire({
              position: 'center',
              icon: 'success',
              title: 'The category has been deleted successfully !',
              showConfirmButton: false,
              timer: 1500
            });
          }
          this.refreshCategoriesList();
        });
      }
    });
  }

  updateCategory(id: any) {
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
        this.http.delete<any>(environment.apiEndpoint + '/categories/' + id, {headers}).subscribe(data => {
          this.status = data.status;
          if (this.status === '1') {
            Swal.fire({
              position: 'center',
              icon: 'success',
              title: 'The category has been deleted successfully !',
              showConfirmButton: false,
              timer: 1500
            });
          }
          this.refreshCategoriesList();
        });
      }
    });
  }
}
