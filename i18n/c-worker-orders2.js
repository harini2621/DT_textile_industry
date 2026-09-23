const {load,save} = require('./lib');
const F='worker-orders.html';
let t = load(F);
t = t.split('placeholder="Search orders..."').join('th:placeholder="#{orders.search.ph}" placeholder="Search orders..."');
save(F,t); console.log('fixed search ph');
