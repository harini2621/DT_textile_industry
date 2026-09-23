package com.textile.smart_textile_tracking_system.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Fallback for every unhandled exception. The full details are logged server-side and
     * only a safe message is shown to the user, so internal information (SQL, stack traces,
     * class names) is never exposed in the browser.
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        logger.error("Unhandled exception while processing request", ex);
        model.addAttribute("errorMessage", userFacingMessage(ex));
        return "error";
    }

    private String userFacingMessage(Exception ex) {
        // Our own services throw these with deliberately human-readable messages.
        if (ex instanceof IllegalArgumentException || ex instanceof IllegalStateException) {
            String message = ex.getMessage();
            if (message != null && !message.isBlank()) {
                return message;
            }
        }
        return "An unexpected error occurred. Please try again.";
    }
}

