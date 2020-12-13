import {Component, OnInit} from '@angular/core';

declare interface RouteInfo {
  path: string;
  title: string;
  icon: string;
  class: string;
}

export const ROUTES: RouteInfo[] = [
  {path: '/products', title: 'Products', icon: 'design_app', class: ''},
  {path: '/cart', title: 'My Cart', icon: 'design_app', class: ''},
  {path: '/sales', title: 'My Commands', icon: 'design_app', class: ''},
  {path: '/profile', title: 'My Profile', icon: 'design_app', class: ''},
];

@Component({
  selector: 'client-app-sidebar',
  templateUrl: './client-sidebar.component.html',
  styleUrls: ['./client-sidebar.component.css']
})
export class ClientSidebarComponent implements OnInit {
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
  }
}
