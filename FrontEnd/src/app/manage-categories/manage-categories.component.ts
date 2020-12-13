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

@Component({
  selector: 'app-manage-categories',
  templateUrl: './manage-categories.component.html',
  styleUrls: ['./manage-categories.component.css']
})
export class ManageCategoriesComponent implements OnInit {
  status = 'empty';
  categoryName: any;
  categories: Category[] = [];
  token: any;

  constructor(private http: HttpClient, private cookieService: CookieService, private router: Router) {
  }

  ngOnInit() {
    const type = this.cookieService.get('type');
    this.token = this.cookieService.get('token');
    if (type === 'client') {
      this.router.navigate(['products']);
    } else if (type === 'admin') {
    } else {
      this.router.navigate(['login']);
    }
    const headers = {'Authorization': this.token};
    this.http.get<any>(environment.apiEndpoint + '/categories', {headers}).subscribe(data => {
      this.categories = data;
    });
  }

  refreshCategoriesList() {
    const headers = {'Authorization': this.token};
    this.http.get<any>(environment.apiEndpoint + '/categories', {headers}).subscribe(data => {
      this.categories = data;
    });
  }

  addCategory() {
    if (this.categoryName === undefined) {
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: 'The fields can\'t be empty !',
        showConfirmButton: false,
        timer: 1500
      });
    } else {
      const headers = {'Authorization': this.token};
      const body = {name: this.categoryName};
      this.http.post<any>(environment.apiEndpoint + '/categories', body, {headers}).subscribe(data => {
        this.status = data.status;
        if (this.status === '1') {
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'The category has been added successfully !',
            showConfirmButton: false,
            timer: 1500
          });
          this.categoryName = '';
        } else if (this.status === '5') {
          Swal.fire({
            position: 'center',
            icon: 'error',
            title: 'This category already exists !',
            showConfirmButton: false,
            timer: 1500
          });
          this.categoryName = '';
        }
        this.refreshCategoriesList();
      });
    }
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
}
