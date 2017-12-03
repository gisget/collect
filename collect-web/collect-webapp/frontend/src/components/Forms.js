import React, { Component } from 'react';
import { FormFeedback, FormGroup, Label, Input, Col } from 'reactstrap';
import L from 'utils/Labels'
import Strings from 'utils/Strings'

export class FormItem extends Component {
    render() {
        const { label, error, touched, asyncValidating } = this.props
        
        return (
            <FormGroup row>
                <Label sm={2}>{label}</Label>
                <Col sm={10} className={asyncValidating ? 'async-validating' : ''}>
                    {this.props.children}
                    {touched && error && <FormFeedback>{error}</FormFeedback>}
                </Col>
            </FormGroup>
        )
    }
}

export default class Forms {

    static handleValidationResponse(r) {
        if (r.statusError) {
            let result = {}
            const errors = r.objects.errors
            if (errors) {
                errors.forEach(error => {
                    result[error.field] = L.l(error.code, error.arguments)
                })
            }
            result._error = L.l('validation.errorsInTheForm') + ': ' + r.errorMessage
            throw result
        }
    }

    static renderFormItemInputField({ input, label, type, contentEditable, meta: { asyncValidating, touched, error } }) {
        return <FormItem label={label} asyncValidating={asyncValidating} touched={touched} error={error}>
                <Input readOnly={contentEditable === false} valid={error ? false : null} {...input} type={type} />
            </FormItem>
    }

    static renderInputField({ input, label, type, contentEditable, meta: { asyncValidating, touched, error } }) {
        return <Input readOnly={contentEditable === false} valid={error ? false : null} {...input} type={type} />
    }

    static renderFormItemSelect({ input, label, type, options, contentEditable, meta: { asyncValidating, touched, error } }) {
        return <FormItem label={label} touched={touched} error={error}>
                <Input readOnly={contentEditable === false} valid={error ? false : null} type="select" {...input}>{options}</Input>
            </FormItem>
    }

    static renderSelect({ input, label, type, options, contentEditable, meta: { asyncValidating, touched, error } }) {
        return <Input readOnly={contentEditable === false} valid={error ? false : null} type="select" {...input}>{options}</Input>
    }

    static normalizeInternalName = value => {
        if (value) {
            return Strings.replaceAll(value.toLowerCase(), '\\W', '_')
        } else {
            return value
        }
    }

}