const {R,load,save} = require('./lib');
const F='admin-dashboard.html';
R(F,"labels: ['Owners', 'Workers', 'Admins', 'Pending Users', 'Tasks', 'Completed Tasks']","labels: [[${#messages.msg('admin.chart.owners')}], [${#messages.msg('admin.chart.workers')}], [${#messages.msg('admin.chart.admins')}], [${#messages.msg('admin.chart.pending')}], [${#messages.msg('admin.chart.tasks')}], [${#messages.msg('admin.chart.completed')}]]");
R(F,"label: 'Count',","label: [[${#messages.msg('admin.chart.count')}]],");
