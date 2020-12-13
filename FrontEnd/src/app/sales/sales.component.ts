import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import Swal from 'sweetalert2';
import {CookieService} from 'ngx-cookie-service';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';

interface Command {
  id: Number;
  user: any;
  date: String;
  time: String;
  address: String;
  status: String;
}

interface CommandItem {
  id: Number;
  item: Number;
  quantity: Number;
  command: Number;
}

interface Category {
  id: Number;
  name: String;
}

@Component({
  selector: 'app-sales',
  templateUrl: './sales.component.html',
  styleUrls: ['./sales.component.css']
})
export class SalesComponent implements OnInit {
  status = 'empty';
  pendingCommands: Command[] = [];
  processedCommands: Command[] = [];
  commandItems: CommandItem[] = [];
  categories: Category[] = [];
  private token: string;

  constructor(private http: HttpClient, private cookieService: CookieService, private router: Router) {
  }

  ngOnInit() {
    this.token = this.cookieService.get('token');
    const type = this.cookieService.get('type');
    if (type === 'client') {
    } else if (type === 'admin') {
    } else {
      this.router.navigate(['login']);
    }
    this.refreshProductsList();
  }

  refreshProductsList() {
    const id = this.cookieService.get('id');
    const headers = {'Authorization': this.token};
    this.http.get<any>(environment.apiEndpoint + '/commands/pending/' + id, {headers}).subscribe(data => {
      this.pendingCommands = data;
      for (const command of this.pendingCommands) {
        this.http.get<any>(environment.apiEndpoint + '/users/' + command.user, {headers}).subscribe(theData => {
          command.user = theData.firstName + ' ' + theData.lastName;
        });
      }
    });
    this.http.get<any>(environment.apiEndpoint + '/commands/processed/' + id, {headers}).subscribe(secondData => {
      this.processedCommands = secondData;
      for (const command of this.processedCommands) {
        this.http.get<any>(environment.apiEndpoint + '/users/' + command.user, {headers}).subscribe(theData => {
          command.user = theData.firstName + ' ' + theData.lastName;
        });
      }
    });
    this.http.get<any>(environment.apiEndpoint + '/categories', {headers}).subscribe(data => {
      this.categories = data;
    });
  }

  viewCommandItems(id: any) {
    const headers = {'Authorization': this.token};
    this.http.get<any>(environment.apiEndpoint + '/commands/' + id + '/items', {headers}).subscribe(data => {
      this.commandItems = data;
      let content = '';
      const size = this.commandItems.length;
      let i = 0;
      for (const item of this.commandItems) {
        i += 1;
        this.http.get<any>(environment.apiEndpoint + '/products/' + item.item, {headers}).subscribe(theData => {
            content += theData.name + '    :    ' + item.quantity + '  item<br>';
            if (i === size) {
              console.log(content);
              Swal.fire({
                title: 'Command Items',
                html: content,
                icon: 'info',
                confirmButtonColor: '#3085d6',
              });
            }
          }
        );
      }
    });
  }
}
