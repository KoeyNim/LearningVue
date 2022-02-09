$(document).ready(function() {
    var app = new Vue({
      el: '#app',
      data: {
        message: '안녕하세요 Vue!',
        board: {
          content: '',
          }
      },
  methods: {
    method1() {
      console.log("111");
    }
  }
    })
    
    var app2 = new Vue({
      el: '#app-2',
      data: {
        message: '이 페이지는 ' + new Date() + ' 에 로드 되었습니다'
      }
})

var app5 = new Vue({
  el: '#app-5',
  data: {
    message: '안녕하세요! Vue.js!'
  },
  methods: {
    reverseMessage: function () {
      this.message = this.message.split('').reverse().join('')
    }
  }
})
});