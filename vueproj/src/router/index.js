import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ContractCount from "@/views/ContractCount";
import OrderCount from "@/views/OrderCount";
import NeverSoldProductCount from "@/views/NeverSoldProductCount";
import FavoriteProductModel from "@/views/FavoriteProductModel";
import AvgStockByCenter from "@/views/AvgStockByCenter";
import ContractInfo from "@/views/ContractInfo";
import ProductByNumber from "@/views/ProductByNumber";
const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/ContractCount',
    name: 'ContractCount',
    component: ContractCount
  },
  {
    path: '/NeverSoldProductCount',
    name: 'NeverSoldProductCount',
    component: NeverSoldProductCount
  },
  {
    path: '/ProductByNumber',
    name: 'ProductByNumber',
    component: ProductByNumber
  },
  {
    path: '/AvgStockByCenter',
    name: 'AvgStockByCenter',
    component: AvgStockByCenter
  },
  {
    path: '/ContractInfo',
    name: 'ContractInfo',
    component: ContractInfo
  },
  {
    path: '/FavoriteProductModel',
    name: 'FavoriteProductModel',
    component: FavoriteProductModel
  },
  {
    path: '/OrderCount',
    name: 'OrderCount',
    component: OrderCount
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
