package com.paypal.android.sdk.onetouch.core;

import android.content.Context;
import android.os.Parcel;

import com.paypal.android.sdk.onetouch.core.config.BillingAgreementRecipe;
import com.paypal.android.sdk.onetouch.core.config.OtcConfiguration;
import com.paypal.android.sdk.onetouch.core.config.Recipe;

public class BillingAgreementRequest extends CheckoutRequest {
    private static final String TAG = BillingAgreementRequest.class.getSimpleName();

    public BillingAgreementRequest() {
        // default constructor needed in addition to parcelable constructor
    }

    public BillingAgreementRequest pairingId(String pairingId) {
        super.pairingId(pairingId);
        return this;
    }

    public BillingAgreementRequest approvalURL(String approvalURL) {
        super.approvalURL(approvalURL);
        return this;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClientMetadataId());
        dest.writeString(getClientId());
        dest.writeString(getEnvironment());

        dest.writeString(mApprovalUrl);
        dest.writeString(mTokenQueryParamKey);
    }

    private BillingAgreementRequest(Parcel source) {
        clientMetadataId(source.readString());
        clientId(source.readString());
        environment(source.readString());

        mApprovalUrl = source.readString();
        mTokenQueryParamKey = source.readString();
    }

    /**
     * required by {@link android.os.Parcelable}
     */
    public static final Creator<BillingAgreementRequest> CREATOR =
            new Creator<BillingAgreementRequest>() {

                @Override
                public BillingAgreementRequest[] newArray(int size) {
                    return new BillingAgreementRequest[size];
                }

                @Override
                public BillingAgreementRequest createFromParcel(Parcel source) {
                    return new BillingAgreementRequest(source);
                }
            };

    @Override
    protected BillingAgreementRequest getThis() {
        return this;
    }

    @Override
    public Recipe getRecipeToExecute(Context context, OtcConfiguration config,
            boolean isSecurityEnabled) {
        for (BillingAgreementRecipe recipe : config.getBillingAgreementRecipes()) {
            if (RequestTarget.wallet == recipe.getTarget()) {
                if (recipe.isValidAppTarget(context, isSecurityEnabled)) {
                    return recipe;
                }
            } else if (RequestTarget.browser == recipe.getTarget()) {
                String browserSwitchUrl = getBrowserSwitchUrl(context, config);

                if (recipe.isValidBrowserTarget(context, browserSwitchUrl)) {
                    return recipe;
                }
            }
        }
        return null;
    }
}
