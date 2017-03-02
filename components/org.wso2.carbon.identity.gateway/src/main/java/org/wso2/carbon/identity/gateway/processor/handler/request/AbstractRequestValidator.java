/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.identity.gateway.processor.handler.request;

import org.wso2.carbon.identity.gateway.processor.FrameworkHandlerResponse;
import org.wso2.carbon.identity.gateway.common.model.sp.RequestValidationConfig;
import org.wso2.carbon.identity.gateway.common.model.sp.RequestValidatorConfig;
import org.wso2.carbon.identity.gateway.context.AuthenticationContext;
import org.wso2.carbon.identity.gateway.processor.handler.FrameworkHandler;
import org.wso2.carbon.identity.gateway.processor.handler.authentication.AuthenticationHandlerException;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public abstract class AbstractRequestValidator extends FrameworkHandler {
    public abstract FrameworkHandlerResponse validate(AuthenticationContext authenticationContext)
            throws RequestValidatorException;

    protected abstract String getValidatorType();

    public RequestValidatorConfig getValidatorConfig(AuthenticationContext authenticationContext) throws RequestValidatorException {

        if (authenticationContext.getServiceProvider() == null) {
            throw new RequestValidatorException("Error while getting validator configs : No service provider " +
                    "found with uniqueId : " + authenticationContext.getUniqueId());
        }
        RequestValidatorConfig validationConfig = null ;

        RequestValidationConfig validatorConfig = authenticationContext.getServiceProvider()
                .getRequestValidationConfig();
        List<RequestValidatorConfig> validatorConfigs = validatorConfig.getRequestValidatorConfigs();

        Iterator<RequestValidatorConfig> validatorConfigIterator = validatorConfigs.iterator();
        while (validatorConfigIterator.hasNext()) {
            RequestValidatorConfig tmpValidationConfig = validatorConfigIterator.next();
            if (getValidatorType().equalsIgnoreCase(tmpValidationConfig.getType())) {
                validationConfig = tmpValidationConfig ;
                break;
            }
        }

        return validationConfig;
    }
}