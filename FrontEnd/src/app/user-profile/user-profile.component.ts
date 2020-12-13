import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import Swal from 'sweetalert2';
import {CookieService} from 'ngx-cookie-service';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  status = 'empty';
  firstName: any;
  lastName: any;
  email: any;
  private token: string;

  constructor(private http: HttpClient, private cookieService: CookieService, private router: Router) {
  }

  ngOnInit() {
    this.token = this.cookieService.get('token');
    const id = this.cookieService.get('id');
    const type = this.cookieService.get('type');
    if (type === 'client') {
      this.router.navigate(['products']);
    } else if (type === 'admin') {
    } else {
      this.router.navigate(['login']);
    }
    const headers = {'Authorization': this.token};
    this.http.get<any>(environment.apiEndpoint + '/users/' + id, {headers}).subscribe(data => {
      this.firstName = data.firstName;
      this.lastName = data.lastName;
      this.email = data.email;
    });
  }

  update() {
    const id = this.cookieService.get('id');
    const body = {firstName: this.firstName, lastName: this.lastName, email: this.email};
    this.http.put<any>(environment.apiEndpoint + '/users/' + id, body).subscribe(data => {
      this.status = data.status;
      if (this.status === '1') {
        Swal.fire({
          position: 'center',
          icon: 'success',
          title: 'Your account has been updated successfully !',
          showConfirmButton: false,
          timer: 1500
        });
      } else {
        Swal.fire({
          position: 'center',
          icon: 'error',
          title: 'An error occurred, please try again ! !',
          showConfirmButton: false,
          timer: 1500
        });
      }
    });
  }
}

