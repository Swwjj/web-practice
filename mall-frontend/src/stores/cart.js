import { defineStore } from 'pinia';
import { getCartCount, getCartList } from '@/api/cart';

export const useCartStore = defineStore('cart', {
  state: () => ({
    cartCount: 0,
    cartList: [],
    totalPrice: 0
  }),
  actions: {
    async fetchCartCount() {
      try {
        const res = await getCartCount();
        if (res.status === 0) {
          this.cartCount = res.data;
        } else {
          this.cartCount = 0;
        }
      } catch (e) {
        this.cartCount = 0;
      }
    },
    async fetchCartList() {
      try {
        const res = await getCartList();
        if (res.status === 0) {
          this.cartList = res.data.lists || [];
          this.totalPrice = res.data.totalPrice || 0;
        } else {
          this.cartList = [];
          this.totalPrice = 0;
        }
      } catch (e) {
        this.cartList = [];
        this.totalPrice = 0;
      }
    }
  }
}); 