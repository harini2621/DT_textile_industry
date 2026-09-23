const {load,save} = require('./lib');
const F='worker-dashboard.html';
let t = load(F);
t = t.split('rounded-pill">\n                                        Accept').join('rounded-pill">\n                                        <span th:text="#{order.accept}">Accept</span>');
t = t.split('rounded-pill">\n                                        Complete').join('rounded-pill">\n                                        <span th:text="#{order.complete}">Complete</span>');
save(F,t); console.log('wdash accept/complete fixed');
