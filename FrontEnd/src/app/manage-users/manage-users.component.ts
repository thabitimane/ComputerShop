import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import Swal from 'sweetalert2';
import {CookieService} from 'ngx-cookie-service';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';

interface Users {
  id: Number;
  firstName: String;
  lastName: String;
  type: String;
  email: String;
  password: String;
}

@Component({
  selector: 'app-manage-users',
  templateUrl: './manage-users.component.html',
  styleUrls: ['./manage-users.component.css']
})
export class ManageUsersComponent implements OnInit {
  status = 'empty';
  firstName: any;
  lastName: any;
  email: any;
  clients: Users[] = [];
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
    this.http.get<any>(environment.apiEndpoint + '/users', {headers}).subscribe(data => {
      this.clients = data;
    });
  }

  refreshProductsList() {
    const headers = {'Authorization': this.token};
    this.http.get<any>(environment.apiEndpoint + '/users', {headers}).subscribe(data => {
      this.clients = data;
    });
  }

  addClient() {
    if (this.firstName === undefined || this.lastName === undefined || this.email === undefined) {
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
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email,
      };
      this.http.post<any>(environment.apiEndpoint + '/users', body, {headers}).subscribe(data => {
        this.status = data.status;
        if (this.status === '1') {
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'The client has been added successfully !',
            showConfirmButton: false,
            timer: 1500
          });
        }
        this.refreshProductsList();
      });
    }
  }

  deleteClient(id: any) {
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
        this.http.delete<any>(environment.apiEndpoint + '/users/' + id, {headers}).subscribe(data => {
          this.status = data.status;
          if (this.status === '1') {
            Swal.fire({
              position: 'center',
              icon: 'success',
              title: 'The client has been deleted successfully !',
              showConfirmButton: false,
              timer: 1500
            });
          }
          this.refreshProductsList();
        });
      }
    });
  }
}
