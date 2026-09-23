const {R,load,save} = require('./lib');
const st = e => "${#messages.msgOrNull('status.' + #strings.replace(#strings.toLowerCase(" + e + "), ' ', '.')) ?: " + e + "}";
// ---- owner-payment-tracking ----
R('owner-payment-tracking.html','placeholder="Amount (₹)"','th:placeholder="#{pay.amount.ph}" placeholder="Amount (₹)"');
R('owner-payment-tracking.html','placeholder="Remarks"','th:placeholder="#{pay.remarks.ph}" placeholder="Remarks"');
R('owner-payment-tracking.html','<i class="bi bi-plus-lg"></i> Save','<i class="bi bi-plus-lg"></i> <span th:text="#{common.save}">Save</span>');
R('owner-payment-tracking.html','th:text="${p.paymentStatus}">Pending</span>','th:text="'+st('p.paymentStatus')+'">Pending</span>');
// ---- owner-production-tracking ----
R('owner-production-tracking.html','</i>Production Batches</h5>','</i><span th:text="#{owner.track.batches}">Production Batches</span></h5>');
R('owner-production-tracking.html','<th>Batch #</th>','<th th:text="#{table.col.batch.no}">Batch #</th>');
R('owner-production-tracking.html','th:text="${stage}" th:selected','th:text="${#messages.msgOrNull(\'stage.\' + #strings.toLowerCase(stage)) ?: stage}" th:selected');
R('owner-production-tracking.html','th:text="${\'Current: \' + batch.currentStage}"></small>','th:text="#{prod.current.prefix(${#messages.msgOrNull(\'stage.\' + #strings.toLowerCase(batch.currentStage)) ?: batch.currentStage})}"></small>');
R('owner-production-tracking.html','th:text="${batch.status}">Pending</span>','th:text="'+st('batch.status')+'">Pending</span>');
R('owner-production-tracking.html','title="Update stage"','th:title="#{owner.track.update.stage}" title="Update stage"');
R('owner-production-tracking.html','title="Assign worker"','th:title="#{owner.track.assign.worker}" title="Assign worker"');
// ---- owner-assign-worker ----
R('owner-assign-worker.html',"th:text=\"${batch.currentStage != null ? batch.currentStage : 'Not started'}\"",'th:text="${batch.currentStage != null ? (#messages.msgOrNull(\'stage.\' + #strings.toLowerCase(batch.currentStage)) ?: batch.currentStage) : #messages.msg(\'stage.not.started\')}"');
// ---- owner-search ----
R('owner-search.html','<span th:if="${productName != null and productName != \'\'}"> for "<span th:text="${productName}"></span>"</span>.','<span th:if="${productName == null or productName == \'\'}" th:text="#{owner.search.no.stock}">No stock found</span><span th:if="${productName != null and productName != \'\'}" th:text="#{owner.search.no.stock.for(${productName})}">No stock found for product</span>.');
// ---- worker-my-tasks ----
R('worker-my-tasks.html','</i>Task List</h5>','</i><span th:text="#{worker.tasks.list}">Task List</span></h5>');
R('worker-my-tasks.html','th:text="${t.status}">Pending</span>','th:text="'+st('t.status')+'">Pending</span>');
// ---- worker-orders ----
R('worker-orders.html','placeholder="Search..."','th:placeholder="#{orders.search.ph}" placeholder="Search..."');
R('worker-orders.html','class="badge" th:text="${order.status}"></span>','class="badge" th:text="'+st('order.status')+'"></span>');
R('worker-orders.html','<i class="bi bi-check-lg"></i> Accept','<i class="bi bi-check-lg"></i> <span th:text="#{order.accept}">Accept</span>');
R('worker-orders.html','<i class="bi bi-x-lg"></i> Reject','<i class="bi bi-x-lg"></i> <span th:text="#{order.reject}">Reject</span>');
R('worker-orders.html','<i class="bi bi-check-circle-fill"></i> Complete','<i class="bi bi-check-circle-fill"></i> <span th:text="#{order.complete}">Complete</span>');
// ---- worker-payment-history ----
R('worker-payment-history.html',"'Order #' + ${p.orderId}",'#{order.no.prefix(${p.orderId})}');
R('worker-payment-history.html','th:text="${p.paymentStatus}">PENDING</span>','th:text="'+st('p.paymentStatus')+'">PENDING</span>');
// ---- worker-work-history ----
R('worker-work-history.html','th:text="${o.status}">Completed</span>','th:text="'+st('o.status')+'">Completed</span>');
// ---- worker-dashboard order badge (raw status) ----
R('worker-dashboard.html','class="badge" th:text="${order.status}"></span>','class="badge" th:text="'+st('order.status')+'"></span>');
// ---- reports chart labels ----
R('reports.html',"labels: [[${#messages.msg('reports.chart.total')}], [[${#messages.msg('reports.chart.completed')}], [[${#messages.msg('reports.chart.pending')}], [[${#messages.msg('reports.chart.other')}],","labels: [ [[${#messages.msg('reports.chart.total')}]], [[${#messages.msg('reports.chart.completed')}]], [[${#messages.msg('reports.chart.pending')}]], [[${#messages.msg('reports.chart.other')}]] ],");
R('reports.html',"label: [[${#messages.msg('reports.chart.orders')}],","label: [[${#messages.msg('reports.chart.orders')}]],");
// ---- image alts ----
R('index.html','alt="Textile Industry"','th:alt="#{img.alt.textile.industry}" alt="Textile Industry"');
R('login.html','alt="Textile"','th:alt="#{img.alt.textile}" alt="Textile"');
R('register.html','alt="Textile"','th:alt="#{img.alt.textile}" alt="Textile"');
