import {Component, OnInit} from '@angular/core';

declare interface RouteInfo {
  path: string;
  title: string;
  icon: string;
  class: string;
}

export const ROUTES: RouteInfo[] = [
  {path: '/manage-products', title: 'Manage Products', icon: 'ui-1_bell-53', class: ''},
  {path: '/manage-categories', title: 'Manage Categories', icon: 'ui-1_bell-53', class: ''},
  {path: '/manage-commands', title: 'Manage Commands', icon: 'ui-1_bell-53', class: ''},
  {path: '/manage-users', title: 'Manage Clients', icon: 'ui-1_bell-53', class: ''},
  {path: '/user-profile', title: 'Admin Profile', icon: 'users_single-02', class: ''},
];

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  menuItems: any[];

  constructor() {
  }

  ngOnInit() {
    this.menuItems = ROUTES.filter(menuItem => menuItem);
  }

  isMobileMenu() {
    if (window.innerWidth > 991) {
      return false;
    }
    return true;
  };
}
