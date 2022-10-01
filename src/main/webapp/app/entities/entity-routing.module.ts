import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'restaurant',
        data: { pageTitle: 'Restaurants' },
        loadChildren: () => import('./restaurantdb/restaurant/restaurant.module').then(m => m.RestaurantdbRestaurantModule),
      },
      {
        path: 'plate',
        data: { pageTitle: 'Plates' },
        loadChildren: () => import('./platdb/plate/plate.module').then(m => m.PlatdbPlateModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
