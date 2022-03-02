const API_VERSION = '/api/v1';
const URLSearch = new URLSearchParams(location.search);
let pageSizeArr = [5, 10, 20, 30, 50];
Vue.use(VeeValidate, {
  locale: 'ko'
});