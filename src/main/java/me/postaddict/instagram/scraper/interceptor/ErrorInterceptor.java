package me.postaddict.instagram.scraper.interceptor;

import me.postaddict.instagram.scraper.ErrorType;
import me.postaddict.instagram.scraper.exception.InstagramAuthException;
import me.postaddict.instagram.scraper.exception.InstagramException;
import me.postaddict.instagram.scraper.exception.InstagramNotFoundException;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class ErrorInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        int code = response.code();

        if (code == 200) {
            return response;
        } else {
            String bodyString = String.valueOf(response.body().string());
            response.body().close();

            ErrorType errorType = ErrorType.UNKNOWN_ERROR;
            switch (code) {
                case 400:
                    if (bodyString.contains("Sorry, you're following the max limit of accounts.")) {
                        errorType = ErrorType.FOLLOWING_THE_MAX_LIMIT_OF_ACCOUNTS;
                    } else if (bodyString.contains("feedback_required") ||
                            bodyString.contains("Action Blocked") ||
                            bodyString.contains("\\u30d6\\u30ed\\u30c3\\u30af\\u3055\\u308c\\u3066\\u3044\\u307e\\u3059")) {
                        errorType = ErrorType.ACTION_BLOCKED;
                    } else if (bodyString.contains("checkpoint_required")) {
                        errorType = ErrorType.CHECKPOINT_REQUIRED;
                    } else if (bodyString.contains("two_factor_required")) {
                        errorType = ErrorType.TWO_FACTOR_REQUIRED;
                    }
                    throw new InstagramException("Bad Request", errorType);
                case 401:
                    throw new InstagramAuthException("Unauthorized", ErrorType.UNAUTHORIZED);
                case 403:
                    if (bodyString.contains("Please wait a few minutes before you try again.") ||
                            bodyString.contains("数分してからもう一度実行してください。")) {
                        errorType = ErrorType.TEMPORARY_ACTION_BLOCKED;
                    } else if (bodyString.contains("unauthorized")) {
                        errorType = ErrorType.UNAUTHORIZED;
                    }
                    throw new InstagramAuthException("Access denied", errorType);
                case 404:
                    throw new InstagramNotFoundException("Resource does not exist", errorType);
                case 429:
                    throw new InstagramException("Rate limited", ErrorType.RATE_LIMITED);
                case 502:
                    throw new InstagramException("Bad Gateway", ErrorType.INSTAGRAM_SERVER_ERROR);
                default:
                    throw new InstagramException("Response code is " + code + ".", errorType);
            }
        }
    }
}
