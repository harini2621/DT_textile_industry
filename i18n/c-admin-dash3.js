const {R,load,save} = require('./lib');
const F='admin-dashboard.html';
R(F,"<small>Welcome back, <strong\n                            th:text=\"${loggedUser != null ? loggedUser.fullName : 'Admin'}\">Admin</strong>!</small>","<small th:text=\"#{admin.dashboard.welcome} + ${loggedUser != null ? ' ' + loggedUser.fullName : ''}\">Welcome back!</small>");
