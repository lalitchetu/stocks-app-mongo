package com.stocksapp.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.stocksapp.api.ErrorCodes;
import com.stocksapp.api.StocksAppError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerAdvisor.class);


    @ExceptionHandler(StocksAppException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public StocksAppError handleTRGatewayException(final StocksAppException objException) {
        LOG.error("Error in processing request {}", objException.getMessage(), objException);
        StocksAppError response = new StocksAppError();
        response.setErrorCodes(objException.getErrorCodes().getErrorCodes());
        response.setErrorDescription(objException.getMessage());
        return response;
    }


    @ExceptionHandler(HttpMessageConversionException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public StocksAppError handleValidation(final HttpMessageConversionException objException) {
        LOG.error("Error in processing request {}", objException.getMessage(), objException);
        Throwable exceptionCause = objException.getCause();
        if (exceptionCause instanceof InvalidDefinitionException) {
            return handleValidationInternal("The following fields are in invalid format: " + listAllFields((InvalidDefinitionException) exceptionCause));
        }
        return handleValidationInternal("Input request message is not Readable");
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public StocksAppError handleValidation(final Throwable objException) {
        LOG.error("Error in processing request {}", objException.getMessage(), objException);
        StocksAppError response = new StocksAppError();
        if (objException.getCause() instanceof StocksAppException) {
            StocksAppException stocksAppException = (StocksAppException) objException.getCause();
            response.setErrorCodes(stocksAppException.getErrorCodes().getErrorCodes());
            response.setErrorDescription(stocksAppException.getMessage());
        } else {
            response.setErrorCodes(ErrorCodes.UNKNOWN_ERROR.getErrorCodes());
            response.setErrorDescription(ErrorCodes.UNKNOWN_ERROR.getErrorDescription());
        }
        return response;
    }

    private StocksAppError handleValidationInternal(String message) {
        StocksAppError response = new StocksAppError();
        response.setErrorCodes(ErrorCodes.INVALID_REQUEST.getErrorCodes());
        response.setErrorDescription(ErrorCodes.INVALID_REQUEST.getErrorDescription() + " " + message);
        return response;
    }

    private String listAllFields(InvalidDefinitionException ex) {
        List<JsonMappingException.Reference> paths = ex.getPath();
        StringBuilder fieldName = new StringBuilder();
        fieldName.append("[ ");
        for (JsonMappingException.Reference path : paths) {
            fieldName.append(path.getFieldName() + ", ");
        }
        fieldName.deleteCharAt(fieldName.lastIndexOf(","));
        fieldName.append(" ]");
        return fieldName.toString();
    }
}
