const {R} = require('./lib');
const F='owner-production-tracking.html';
R(F,'Order No.</th>','<span th:text="#{table.col.orderNo}">Order No.</span></th>');
