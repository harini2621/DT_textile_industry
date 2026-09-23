const {load,save} = require('./lib');
const F='owner-production-tracking.html';
let t = load(F);
const reps=[
['Total Batches</small>','<span th:text="#{prod.total.batches}">Total Batches</span></small>'],
['>Pending</small>',' th:text="#{stat.pending}">Pending</small>'],
['>In Progress</small>',' th:text="#{stat.in.progress}">In Progress</small>'],
['>Completed</small>',' th:text="#{stat.completed}">Completed</small>'],
['Order No.</th>','<span th:text="#{table.col.orderNo}">Order No.</span></th>'],
];
for(const [a,b] of reps){ const n=t.split(a).length-1; t=t.split(a).join(b); console.log(n+' :: '+a.slice(0,40)); }
save(F,t);
