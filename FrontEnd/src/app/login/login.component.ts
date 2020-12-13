import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CookieService} from 'ngx-cookie-service';
import Swal from 'sweetalert2';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  data: Date = new Date();
  focus;
  focus1;
  email: any;
  password: any;
  status = '';

  constructor(private http: HttpClient, private cookieService: CookieService, private router: Router) {
  }

  ngOnInit() {
    const type = this.cookieService.get('type');
    if (type === 'client') {
      this.router.navigate(['products']);
    } else if (type === 'admin') {
      this.router.navigate(['manage-products']);
    }
  }

  register() {
    this.router.navigate(['register']);
  }

  login() {
    if (this.email === undefined || this.password === undefined) {
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: 'The fields can\'t be empty !',
        showConfirmButton: false,
        timer: 1500
      });
    } else {
      const body = {email: this.email, password: this.password};
      this.http.post<any>(environment.apiEndpoint + '/login', body).subscribe(data => {
        this.status = data.status;
        if (this.status === '1') {
          this.cookieService.set('token', data.token);
          this.cookieService.set('type', data.type);
          this.cookieService.set('id', data.id);
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'Logged in successfully !',
            showConfirmButton: false,
            timer: 1500
          });
          if (data.type === 'client') {
            this.router.navigate(['products']);
          } else if (data.type === 'admin') {
            this.router.navigate(['manage-products']);
          }
        } else if (this.status === '5') {
          Swal.fire({
            position: 'center',
            icon: 'warning',
            title: 'You have to verify you email firstly !',
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
