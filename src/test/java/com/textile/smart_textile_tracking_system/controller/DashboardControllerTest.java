package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.dto.DashboardStats;
import com.textile.smart_textile_tracking_system.entity.Order;
import com.textile.smart_textile_tracking_system.entity.User;
import com.textile.smart_textile_tracking_system.service.DashboardService;
import com.textile.smart_textile_tracking_system.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Controller-layer tests for the dashboard endpoints. Uses standalone MockMvc so
 * the Thymeleaf rendering layer (which requires the full app context and real
 * layout fragments) is skipped; view names and model attributes are still verified.
 */
class DashboardControllerTest {

    private MockMvc mockMvc;

    private final DashboardService dashboardService = Mockito.mock(DashboardService.class);
    private final UserService userService = Mockito.mock(UserService.class);

    @BeforeEach
    void setUp() {
        DashboardController controller = new DashboardController();
        org.springframework.test.util.ReflectionTestUtils.setField(controller, "dashboardService", dashboardService);
        org.springframework.test.util.ReflectionTestUtils.setField(controller, "userService", userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(new org.springframework.web.servlet.ViewResolver() {
                    @Override
                    public org.springframework.web.servlet.View resolveViewName(String viewName,
                            java.util.Locale locale) {
                        return new org.springframework.web.servlet.view.AbstractView() {
                            @Override
                            protected void renderMergedOutputModel(java.util.Map<String, Object> model,
                                    jakarta.servlet.http.HttpServletRequest request,
                                    jakarta.servlet.http.HttpServletResponse response) {
                                // no-op: rendering is not under test
                            }
                        };
                    }
                })
                .setCustomArgumentResolvers(new org.springframework.web.method.support.HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(org.springframework.core.MethodParameter parameter) {
                        return parameter.hasParameterAnnotation(
                                org.springframework.security.core.annotation.AuthenticationPrincipal.class);
                    }

                    @Override
                    public Object resolveArgument(org.springframework.core.MethodParameter parameter,
                                                  org.springframework.web.method.support.ModelAndViewContainer mavContainer,
                                                  org.springframework.web.context.request.NativeWebRequest webRequest,
                                                  org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
                        return webRequest.getUserPrincipal() instanceof org.springframework.security.authentication.UsernamePasswordAuthenticationToken token
                                ? token.getPrincipal()
                                : webRequest.getUserPrincipal();
                    }
                })
                .build();

        // Simulate an authenticated principal for @AuthenticationPrincipal resolution
        UserDetails principal = new org.springframework.security.core.userdetails.User(
                "testuser", "password", List.of());
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private static User user() {
        User u = new User();
        u.setUsername("testuser");
        return u;
    }

    @Test
    void ownerDashboardReturnsViewAndModelAttributes() throws Exception {
        DashboardStats stats = new DashboardStats();
        stats.setTotalOrders(2L);
        stats.setPendingOrders(1L);
        stats.setInProgressOrders(1L);
        stats.setCompletedOrders(0L);
        stats.setTotalStock(7L);
        stats.setTotalWorkers(3L);
        stats.setRecentOrders(List.of(new Order()));

        when(dashboardService.getOwnerDashboardStats("testuser")).thenReturn(stats);
        when(userService.getUserByUsername("testuser")).thenReturn(Optional.of(user()));

        mockMvc.perform(get("/owner-dashboard").principal(SecurityContextHolder.getContext().getAuthentication()))
                .andExpect(status().isOk())
                .andExpect(view().name("owner-dashboard"))
                .andExpect(model().attribute("totalOrders", 2L))
                .andExpect(model().attribute("pendingOrders", 1L))
                .andExpect(model().attribute("inProgressOrders", 1L))
                .andExpect(model().attribute("completedOrders", 0L))
                .andExpect(model().attribute("totalStock", 7L))
                .andExpect(model().attribute("totalWorkers", 3L))
                .andExpect(model().attribute("recentOrders", hasSize(1)))
                .andExpect(model().attributeExists("loggedUser"));
    }

    @Test
    void ownerDashboardWithoutKnownUserOmitsLoggedUser() throws Exception {
        DashboardStats stats = new DashboardStats();
        stats.setRecentOrders(List.of());
        when(dashboardService.getOwnerDashboardStats("testuser")).thenReturn(stats);
        when(userService.getUserByUsername("testuser")).thenReturn(Optional.empty());

        mockMvc.perform(get("/owner-dashboard").principal(SecurityContextHolder.getContext().getAuthentication()))
                .andExpect(status().isOk())
                .andExpect(view().name("owner-dashboard"))
                .andExpect(model().attributeDoesNotExist("loggedUser"));
    }

    @Test
    void workerDashboardReturnsViewAndModelAttributes() throws Exception {
        DashboardStats stats = new DashboardStats();
        stats.setAssignedTasks(3L);
        stats.setCompletedTasks(1L);
        stats.setPendingTasks(1L);
        stats.setMyStockCount(10L);
        stats.setMyOrders(List.of(new Order()));

        when(dashboardService.getWorkerDashboardStats("testuser")).thenReturn(stats);
        when(userService.getUserByUsername("testuser")).thenReturn(Optional.of(user()));

        mockMvc.perform(get("/worker-dashboard").principal(SecurityContextHolder.getContext().getAuthentication()))
                .andExpect(status().isOk())
                .andExpect(view().name("worker-dashboard"))
                .andExpect(model().attribute("assignedTasks", 3L))
                .andExpect(model().attribute("completedTasks", 1L))
                .andExpect(model().attribute("pendingTasks", 1L))
                .andExpect(model().attribute("myStockCount", 10L))
                .andExpect(model().attribute("myOrders", hasSize(1)))
                .andExpect(model().attributeExists("loggedUser"));

        verify(dashboardService).getWorkerDashboardStats("testuser");
    }
}
