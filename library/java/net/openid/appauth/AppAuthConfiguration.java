/*
 * Copyright 2016 The AppAuth for Android Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.openid.appauth;

import androidx.annotation.NonNull;

import net.openid.appauth.browser.AnyBrowserMatcher;
import net.openid.appauth.browser.BrowserMatcher;
import net.openid.appauth.connectivity.ConnectionBuilder;
import net.openid.appauth.connectivity.DefaultConnectionBuilder;

/**
 * Defines configuration properties that control the behavior of the AppAuth library, independent
 * of the OAuth2 specific details that are described.
 */
public class AppAuthConfiguration {

    /**
     * The default configuration that is used if no configuration is explicitly specified
     * when constructing an {@link AuthorizationService}.
     */
    public static final AppAuthConfiguration DEFAULT =
            new AppAuthConfiguration.Builder().build();

    @NonNull
    private final BrowserMatcher mBrowserMatcher;

    @NonNull
    private final ConnectionBuilder mConnectionBuilder;

    private final boolean mSkipIssuerHttpsCheck;

    private final boolean mSkipAudienceAndNonceValidation;

    private AppAuthConfiguration(
            @NonNull BrowserMatcher browserMatcher,
            @NonNull ConnectionBuilder connectionBuilder,
            Boolean skipIssuerHttpsCheck,
            Boolean skipAudienceAndNonceValidation) {
        mBrowserMatcher = browserMatcher;
        mConnectionBuilder = connectionBuilder;
        mSkipIssuerHttpsCheck = skipIssuerHttpsCheck;
        mSkipAudienceAndNonceValidation = skipAudienceAndNonceValidation;
    }

    /**
     * Controls which browsers can be used for the authorization flow.
     */
    @NonNull
    public BrowserMatcher getBrowserMatcher() {
        return mBrowserMatcher;
    }

    /**
     * Creates {@link java.net.HttpURLConnection} instances for use in token requests and related
     * interactions with the authorization service.
     */
    @NonNull
    public ConnectionBuilder getConnectionBuilder() {
        return mConnectionBuilder;
    }

    /**
     * Returns <code>true</code> if issuer https validation is disabled, otherwise
     * <code>false</code>.
     *
     * @see Builder#setSkipIssuerHttpsCheck(Boolean)
     */
    public boolean getSkipIssuerHttpsCheck() { return mSkipIssuerHttpsCheck; }

    /**
     * Returns <code>true</code> if ID token audience and nonce validation is disabled, otherwise
     * <code>false</code>.
     *
     * @see Builder#setSkipAudienceAndNonceValidation(Boolean)
     */
    public boolean getSkipAudienceAndNonceValidation() { return mSkipAudienceAndNonceValidation; }

    /**
     * Creates {@link AppAuthConfiguration} instances.
     */
    public static class Builder {

        private BrowserMatcher mBrowserMatcher = AnyBrowserMatcher.INSTANCE;
        private ConnectionBuilder mConnectionBuilder = DefaultConnectionBuilder.INSTANCE;
        private boolean mSkipIssuerHttpsCheck;
        private boolean mSkipAudienceAndNonceValidation;

        /**
         * Specify the browser matcher to use, which controls the browsers that can be used
         * for authorization.
         */
        @NonNull
        public Builder setBrowserMatcher(@NonNull BrowserMatcher browserMatcher) {
            Preconditions.checkNotNull(browserMatcher, "browserMatcher cannot be null");
            mBrowserMatcher = browserMatcher;
            return this;
        }

        /**
         * Specify the connection builder to use, which creates {@link java.net.HttpURLConnection}
         * instances for use in direct communication with the authorization service.
         */
        @NonNull
        public Builder setConnectionBuilder(@NonNull ConnectionBuilder connectionBuilder) {
            Preconditions.checkNotNull(connectionBuilder, "connectionBuilder cannot be null");
            mConnectionBuilder = connectionBuilder;
            return this;
        }

        /**
         * Disables https validation for the issuer identifier.
         *
         * <p>NOTE: Disabling issuer https validation implies the app is running against an
         * insecure environment. Enabling this option is only recommended for testing purposes.
         */
        public Builder setSkipIssuerHttpsCheck(Boolean skipIssuerHttpsCheck) {
            mSkipIssuerHttpsCheck = skipIssuerHttpsCheck;
            return this;
        }

        /**
         * Disables the audience/azp (rule #3) and nonce (rule #11) checks performed when
         * validating the ID token returned as part of a token response. The issuer, expiry and
         * issued-at checks remain enforced.
         *
         * <p>WARNING: Skipping the audience and nonce checks (OpenID Connect Core Section 3.1.3.7
         * rules #3 and #11) defeats protections against token substitution and replay attacks.
         * Only enable this option for non-standard providers you control and trust, or for
         * testing purposes.
         */
        public Builder setSkipAudienceAndNonceValidation(Boolean skipAudienceAndNonceValidation) {
            mSkipAudienceAndNonceValidation = skipAudienceAndNonceValidation;
            return this;
        }

        /**
         * Creates the instance from the configured properties.
         */
        @NonNull
        public AppAuthConfiguration build() {
            return new AppAuthConfiguration(
                mBrowserMatcher,
                mConnectionBuilder,
                mSkipIssuerHttpsCheck,
                mSkipAudienceAndNonceValidation
            );
        }


    }
}
