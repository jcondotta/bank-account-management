package com.jcondotta.interfaces.rest.exception_handler;

//@Produces
//@Singleton
//@Replaces(value = ConstraintExceptionHandler.class)
//@Requires(classes = { ConstraintViolationException.class, ExceptionHandler.class })
public class RestConstraintExceptionHandler {
        //extends ConstraintExceptionHandler {

//    private static final Logger LOGGER = LoggerFactory.getLogger(RestConstraintExceptionHandler.class);
//    private final MessageSource messageSource;
//    private final ErrorResponseProcessor<?> errorResponseProcessor;
//
//    public RestConstraintExceptionHandler(MessageSource messageSource, ErrorResponseProcessor<?> errorResponseProcessor) {
//        super(errorResponseProcessor);
//        this.messageSource = messageSource;
//        this.errorResponseProcessor = errorResponseProcessor;
//    }
//
//    @Override
//    @Status(value = HttpStatus.BAD_REQUEST)
//    public HttpResponse<?> handle(HttpRequest request, ConstraintViolationException exception) {
//        var locale = Locale.getDefault();
//
//        List<String> errorMessages = new ArrayList<>();
//        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
//            var message = violation.getMessage();
//
//            var localizedMessage = messageSource.getMessage(message, locale).orElse(message);
//            errorMessages.add(localizedMessage);
//            LOGGER.error(localizedMessage);
//        }
//
//        var responseBody = Map.of(
//                "_embedded", Map.of("errors", errorMessages.stream()
//                        .map(msg -> Map.of("message", msg))
//                        .toList()),
//                "message", "Bad Request"
//        );
//
//        return errorResponseProcessor.processResponse(
//                ErrorContext.builder(request)
//                        .cause(exception)
//                        .errorMessages(errorMessages)
//                        .build(),
//                HttpResponse.badRequest().body(responseBody)
//        );
//    }
}