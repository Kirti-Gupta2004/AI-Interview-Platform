import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard'; 
// (Agar folder ka naam past-questions hai toh wo upar component ke hisab se import hoga)

export const routes: Routes = [
  { 
    path: '', 
    component: DashboardComponent // 👈 Jab localhost:4200 khule, toh seedhe Dashboard dikhe
  },
  {
    path: '**',
    redirectTo: '' // 👈 Agar galat path ho toh bhi main page par bhej de
  }
];