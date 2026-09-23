const {R,load,save} = require('./lib');
const ROLE = "${#messages.msgOrNull('role.' + #strings.toLowerCase(u.role)) ?: u.role}";
const ACT = "${#messages.msgOrNull('admin.action.' + #strings.toLowerCase(log.action)) ?: log.action}";
const st = e => "${#messages.msgOrNull('status.' + #strings.replace(#strings.toLowerCase(" + e + "), ' ', '.')) ?: " + e + "}";
// ---- admin-activity-logs ----
R('admin-activity-logs.html','<i class="bi bi-clock-history"></i> Activity Logs</a>','<i class="bi bi-clock-history"></i> <span th:text="#{nav.activity.logs}">Activity Logs</span></a>');
// ---- admin-users ----
R('admin-users.html','No pending users. Everyone is approved!','<span th:text="#{admin.users.empty}">No pending users. Everyone is approved!</span>');
R('admin-users.html','</i>All Users</h5>','</i><span th:text="#{admin.users.allUsers}">All Users</span></h5>');
R('admin-users.html','th:if="${u.approved}">Approved</span>','th:if="${u.approved}" th:text="#{admin.users.statusApproved}">Approved</span>');
R('admin-users.html','th:unless="${u.approved}">Pending</span>','th:unless="${u.approved}" th:text="#{admin.users.statusPending}">Pending</span>');
R('admin-users.html','</i>No users found.','</i><span th:text="#{admin.users.emptyAll}">No users found.</span>');
R('admin-users.html','th:text="${u.role}"','th:text="'+ROLE+'"');
// ---- admin-dashboard ----
R('admin-dashboard.html','th:text="${u.role}"','th:text="'+ROLE+'"');
R('admin-dashboard.html','th:text="${log.action}">ACTION</span>','th:text="'+ACT+'">ACTION</span>');
R('admin-dashboard.html',"labels: [[${#messages.msg('admin.chart.owners')}], [${#messages.msg('admin.chart.workers')}], [${#messages.msg('admin.chart.admins')}], [${#messages.msg('admin.chart.pending')}], [${#messages.msg('admin.chart.tasks')}], [${#messages.msg('admin.chart.completed')}]],","labels: [ [[${#messages.msg('admin.chart.owners')}]], [[${#messages.msg('admin.chart.workers')}]], [[${#messages.msg('admin.chart.admins')}]], [[${#messages.msg('admin.chart.pending')}]], [[${#messages.msg('admin.chart.tasks')}]], [[${#messages.msg('admin.chart.completed')}]] ],");
// ---- owner-dashboard ----
R('owner-dashboard.html','<p>Workers</p>','<p th:text="#{owner.stat.total.workers}">Workers</p>');
R('owner-dashboard.html','<span class="badge bg-primary">Live</span>','<span class="badge bg-primary" th:text="#{owner.live}">Live</span>');
R('owner-dashboard.html','<small class="text-muted">Total Orders</small>','<small class="text-muted" th:text="#{owner.stat.total.orders}">Total Orders</small>');
R('owner-dashboard.html','<small class="text-muted">Pending Orders</small>','<small class="text-muted" th:text="#{owner.stat.pending.orders}">Pending Orders</small>');
R('owner-dashboard.html','<small class="text-muted">Completed Orders</small>','<small class="text-muted" th:text="#{owner.stat.completed.orders}">Completed Orders</small>');
R('owner-dashboard.html','class="badge" th:text="${order.status}"></span>','class="badge" th:text="'+st('order.status')+'"></span>');
R('owner-dashboard.html',"labels: ['Pending', 'Completed', 'Other'],","labels: [ [[${#messages.msg('status.pending')}]], [[${#messages.msg('status.completed')}]], [[${#messages.msg('reports.chart.other')}]] ],");
R('owner-dashboard.html','<h5 class="fw-bold mb-0" th:text="#{owner.card.status.overview}"><span th:text="#{owner.card.status.overview}">Order Status Overview</span></h5>','<h5 class="fw-bold mb-0" th:text="#{owner.card.status.overview}">Order Status Overview</h5>');
// ---- owner-orders ----
R('owner-orders.html','class="badge" th:text="${order.status}"></span>','class="badge" th:text="'+st('order.status')+'"></span>');
