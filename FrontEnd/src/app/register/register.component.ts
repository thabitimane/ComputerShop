import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CookieService} from 'ngx-cookie-service';
import Swal from 'sweetalert2';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  data: Date = new Date();
  focus;
  focus1;
  firstName: any;
  lastName: any;
  email: any;
  password: any;
  repeatPassword: any;
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

  login() {
    this.router.navigate(['login']);
  }

  register() {
    if (this.firstName === undefined || this.lastName === undefined || this.email === undefined || this.password === undefined) {
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: 'The fields can\'t be empty !',
        showConfirmButton: false,
        timer: 1500
      });
    } else {
      const body = {firstName: this.firstName, lastName: this.lastName, email: this.email, password: this.password};
      this.http.post<any>(environment.apiEndpoint + '/register', body).subscribe(data => {
        this.status = data.status;
        if (this.status === '1') {
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'Your account has been created successfully !',
            showConfirmButton: false,
            timer: 1500
          });
          this.login();
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
