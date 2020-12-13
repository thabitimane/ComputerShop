import {NgModule} from '@angular/core';
import {CommonModule,} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {Routes, RouterModule} from '@angular/router';
import {UserProfileComponent} from './user-profile/user-profile.component';
import {ManageProductsComponent} from './manage-products/manage-products.component';
import {ManageCategoriesComponent} from './manage-categories/manage-categories.component';
import {ProductsComponent} from './products/products.component';
import {ManageUsersComponent} from './manage-users/manage-users.component';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {CartComponent} from './cart/cart.component';
import {SalesComponent} from './sales/sales.component';
import {ProfileComponent} from './profile/profile.component';
import {ManageCommandsComponent} from './manage-commands/manage-commands.component';

const routes: Routes = [
  {path: 'user-profile', component: UserProfileComponent},
  {path: 'manage-products', component: ManageProductsComponent},
  {path: 'manage-categories', component: ManageCategoriesComponent},
  {path: 'manage-commands', component: ManageCommandsComponent},
  {path: 'manage-users', component: ManageUsersComponent},
  {path: 'products', component: ProductsComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'cart', component: CartComponent},
  {path: 'sales', component: SalesComponent},
  {path: 'profile', component: ProfileComponent},
  {path: '**', redirectTo: 'products'}
];

@NgModule({
  imports: [
    CommonModule,
    BrowserModule,
    RouterModule.forRoot(routes)
  ],
  exports: [],
})
export class AppRoutingModule {
}
