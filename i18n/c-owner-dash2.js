const {R} = require('./lib');
const F='owner-dashboard.html';
// sidebar lang links (after Reports item)
R(F,'<li><a href="/profile"><i class="bi bi-person-circle"></i> <span th:text="#{nav.profile}">Profile</span></a></li>',
'<li><a href="?lang=en"><i class="bi bi-globe2"></i> <span th:text="#{lang.en}">English</span></a></li>\n            <li><a href="?lang=ta"><i class="bi bi-globe2"></i> <span th:text="#{lang.ta}">\u0ba4\u0bae\u0bbf\u0bb4\u0bcd</span></a></li>\n            <li><a href="/profile"><i class="bi bi-person-circle"></i> <span th:text="#{nav.profile}">Profile</span></a></li>');
// stats
R(F,'<p>Total Workers</p>','<p th:text="#{owner.stat.total.workers}">Total Workers</p>');
R(F,'<p>Pending Orders</p>','<p th:text="#{owner.stat.pending.orders}">Pending Orders</p>');
R(F,'<h5 class="fw-bold mb-0">Order Status Overview</h5>','<h5 class="fw-bold mb-0" th:text="#{owner.card.status.overview}">Order Status Overview</h5>');
R(F,'<span class="live-badge">Live</span>','<span class="live-badge" th:text="#{admin.dashboard.live}">Live</span>');
R(F,'<h5 class="fw-bold mb-0">Business Summary</h5>','<h5 class="fw-bold mb-0" th:text="#{owner.card.summary}">Business Summary</h5>');
R(F,'<span>Total Orders</span>','<span th:text="#{report.metric.totalOrders}">Total Orders</span>');
R(F,'<span>Completed Orders</span>','<span th:text="#{owner.card.completed.orders}">Completed Orders</span>');
R(F,'<span>Pending Orders</span>','<span th:text="#{owner.stat.pending.orders}">Pending Orders</span>');
R(F,'<span>Total Workers</span>','<span th:text="#{report.metric.totalWorkers}">Total Workers</span>');
R(F,'<h5 class="fw-bold mb-0">Recent Orders</h5>','<h5 class="fw-bold mb-0" th:text="#{owner.card.recent.orders}">Recent Orders</h5>');
R(F,'<a href="/owner-orders" class="btn btn-sm btn-outline-primary rounded-pill">View All</a>','<a href="/owner-orders" class="btn btn-sm btn-outline-primary rounded-pill" th:text="#{admin.dashboard.viewAll}">View All</a>');
// table headers
R(F,'<th>#</th>','<th th:text="#{table.col.num}">#</th>');
R(F,'<th>Product</th>','<th th:text="#{table.col.product}">Product</th>');
R(F,'<th>Quantity</th>','<th th:text="#{table.col.quantity}">Quantity</th>');
R(F,'<th>Worker</th>','<th th:text="#{th.worker}">Worker</th>');
R(F,'<th>Request Date</th>','<th th:text="#{table.col.request.date}">Request Date</th>');
R(F,'<th>Status</th>','<th th:text="#{common.status}">Status</th>');
// empty + quick actions
R(F,'<i class="bi bi-inbox display-6 d-block mb-2"></i>No orders yet.','<i class="bi bi-inbox display-6 d-block mb-2"></i><span th:text="#{owner.empty.no.orders}">No orders yet.</span>');
R(F,'<a href="/create-order" class="btn btn-primary btn-sm mt-2 rounded-pill">Create First Order</a>','<a href="/create-order" class="btn btn-primary btn-sm mt-2 rounded-pill" th:text="#{owner.btn.create.first}">Create First Order</a>');
R(F,'<h5 class="mt-3">Search Inventory</h5>','<h5 class="mt-3" th:text="#{owner.card.search}">Search Inventory</h5>');
R(F,'<p class="text-muted mb-3">Monitor stock availability in real time.</p>','<p class="text-muted mb-3" th:text="#{owner.card.search.desc}">Monitor stock availability in real time.</p>');
R(F,'<a href="/owner-search" class="btn btn-outline-primary rounded-pill">View Stock</a>','<a href="/owner-search" class="btn btn-outline-primary rounded-pill" th:text="#{owner.card.view.stock}">View Stock</a>');
R(F,'<h5 class="mt-3">Manage Orders</h5>','<h5 class="mt-3" th:text="#{owner.card.manage}">Manage Orders</h5>');
R(F,'<p class="text-muted mb-3">Review and track all customer orders.</p>','<p class="text-muted mb-3" th:text="#{owner.card.manage.desc}">Review and track all customer orders.</p>');
R(F,'<a href="/owner-orders" class="btn btn-outline-success rounded-pill">Open Orders</a>','<a href="/owner-orders" class="btn btn-outline-success rounded-pill" th:text="#{owner.card.open.orders}">Open Orders</a>');
R(F,'<h5 class="mt-3">Business Reports</h5>','<h5 class="mt-3" th:text="#{owner.card.biz.reports}">Business Reports</h5>');
R(F,'<p class="text-muted mb-3">Analyze production and business performance.</p>','<p class="text-muted mb-3" th:text="#{owner.card.biz.desc}">Analyze production and business performance.</p>');
R(F,'<a href="/reports" class="btn btn-outline-warning rounded-pill">View Reports</a>','<a href="/reports" class="btn btn-outline-warning rounded-pill" th:text="#{owner.card.view.reports}">View Reports</a>');
