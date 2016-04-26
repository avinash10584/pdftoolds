package org.pdftools.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerformanceLogger {
    
    static org.slf4j.Logger logger = LoggerFactory.getLogger("perfLogger");
    
   /* @Around("execution(* gov.uspto.pe2e.oc.pdf.PdfWorker(..))")
    public Object logPdfMethods(final ProceedingJoinPoint joinPoint) throws Throwable {
        final long startMillis = System.currentTimeMillis();
        try {
            final Object retVal = joinPoint.proceed();
            return retVal;
        } finally {
            final long duration = System.currentTimeMillis() - startMillis;
            logger.info("PDF: {} | {} ms", joinPoint.getSignature(), duration);
        }
    }
    @Around("execution(* gov.uspto.pe2e.oc.authoring.service.*.*(..)) || "
            + "execution(* gov.uspto.pe2e.oc.workflow.service.*.*(..)) || "
            + "execution(* gov.uspto.pe2e.oc.workflow.mailroom.service.*.*(..))")
    public Object logServiceMethods(final ProceedingJoinPoint joinPoint) throws Throwable {
        final long startMillis = System.currentTimeMillis();
        try {
            final Object retVal = joinPoint.proceed();
            return retVal;
        } finally {
            final long duration = System.currentTimeMillis() - startMillis;
            logger.info("Service: {} | {} ms", joinPoint.getSignature(), duration);
        }
    }
    
    /*@Around("execution(* gov.uspto.pe2e.oc.authoring.dao.impl.*(..)) || "
            + "execution(* gov.uspto.pe2e.oc.authoring.mapper.*(..)) || "
            + "execution(* gov.uspto.pe2e.oc.authoring.dao.jpa.*(..))")
    public Object logDAOMethods(final ProceedingJoinPoint joinPoint) throws Throwable {
        final long startMillis = System.currentTimeMillis();
        try {
            final Object retVal = joinPoint.proceed();
            return retVal;
        } finally {
            final long duration = System.currentTimeMillis() - startMillis;
            logger.info("DAO: {} | {} ms", joinPoint.getSignature(), duration);
        }
    }*/
}