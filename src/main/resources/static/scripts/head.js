const API_VERSION = '/api/v1';
const URLSearch = new URLSearchParams(location.search);
let pageSizeArr = [1, 5, 10, 20, 30, 50];
let header;
Vue.use(VeeValidate, {
  locale: 'ko'
});

$().ready(() => {
  document.title = $('.pagetit').text() || $('.login-tit').text()

  header = new Vue({
    el: '#header',
    data: {
        toggle: false
    },
    methods: {
      fnToggleSidebar(e) {
        console.log('fnToggleSidebar', arguments)
        let me = this;
        me.toggle = !me.toggle;
      },
    }
  })
});