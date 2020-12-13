import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {ToastrModule} from 'ngx-toastr';
import {AppRoutingModule} from './app.routing';
import {ComponentsModule} from './components/components.module';
import {AppComponent} from './app.component';
import {ChartsModule} from 'ng2-charts/ng2-charts';
import {UserProfileComponent} from './user-profile/user-profile.component';
import {ManageProductsComponent} from './manage-products/manage-products.component';
import {ManageCategoriesComponent} from './manage-categories/manage-categories.component';
import {ProductsComponent} from './products/products.component';
import {ManageUsersComponent} from './manage-users/manage-users.component';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {CartComponent} from './cart/cart.component';
import {ProfileComponent} from './profile/profile.component';
import {SalesComponent} from './sales/sales.component';
import {ManageCommandsComponent} from './manage-commands/manage-commands.component';

@NgModule({
  imports: [
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    ComponentsModule,
    RouterModule,
    AppRoutingModule,
    NgbModule,
    ChartsModule,
    ToastrModule.forRoot()
  ],
  declarations: [
    AppComponent,
    UserProfileComponent,
    ManageProductsComponent,
    ManageCategoriesComponent,
    ManageUsersComponent,
    ManageCommandsComponent,
    LoginComponent,
    RegisterComponent,
    CartComponent,
    ProfileComponent,
    SalesComponent,
    ProductsComponent,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
