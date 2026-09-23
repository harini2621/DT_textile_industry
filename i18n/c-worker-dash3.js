const {load,save} = require('./lib');
const F='worker-dashboard.html';
let t = load(F);
const reps=[
['My Assigned Orders</h5>','<span th:text="#{worker.orders.assigned.heading}">My Assigned Orders</span></h5>'],
['>View All</a>',' th:text="#{common.view.all}">View All</a>'],
['</i> Accept','</i> <span th:text="#{order.accept}">Accept</span>'],
['</i> Complete','</i> <span th:text="#{order.complete}">Complete</span>'],
];
for(const [a,b] of reps){ const n=t.split(a).length-1; t=t.split(a).join(b); console.log(n+' :: '+a); }
save(F,t);
