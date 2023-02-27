package com.dkb.urlshortener.utils.validation

import com.dkb.urlshortener.utils.URLValidator
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@Constraint(validatedBy = [UrlValidatorAnnotation::class])
annotation class ValidUrl(
    val message: String = "Url Provided is not valid",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class UrlValidatorAnnotation(
    private val urlValidator: URLValidator
) : ConstraintValidator<ValidUrl, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return urlValidator.validateURL(value)
    }
}
