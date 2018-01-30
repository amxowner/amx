/*
 * Copyright © 2013-2016 The Nxt Core Developers.
 * Copyright © 2016 Jelurida IP B.V.
 *
 * See the LICENSE.txt file at the top-level directory of this distribution
 * for licensing information.
 *
 * Unless otherwise agreed in a custom licensing agreement with Jelurida B.V.,
 * no part of the Nxt software, including this file, may be copied, modified,
 * propagated, or distributed except according to the terms contained in the
 * LICENSE.txt file.
 *
 * Removal or modification of this copyright notice is prohibited.
 *
 */

package amx.http;

import amx.Account;
import amx.Appendix;
import amx.Attachment;
import amx.Constants;
import amx.AmxException;
import amx.util.Convert;
import amx.util.JSON;
import org.apache.tika.Tika;
import org.apache.tika.mime.MediaType;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

import static amx.http.JSONResponses.INCORRECT_DGS_LISTING_DESCRIPTION;
import static amx.http.JSONResponses.INCORRECT_DGS_LISTING_NAME;
import static amx.http.JSONResponses.INCORRECT_DGS_LISTING_TAGS;
import static amx.http.JSONResponses.MISSING_NAME;

public final class DGSListing extends CreateTransaction {

    static final DGSListing instance = new DGSListing();

    private DGSListing() {
        super("messageFile", new APITag[] {APITag.DGS, APITag.CREATE_TRANSACTION},
                "name", "description", "tags", "quantity", "priceNQT");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws AmxException {

        String name = Convert.emptyToNull(req.getParameter("name"));
        String description = Convert.nullToEmpty(req.getParameter("description"));
        String tags = Convert.nullToEmpty(req.getParameter("tags"));
        long priceNQT = ParameterParser.getPriceNQT(req);
        int quantity = ParameterParser.getGoodsQuantity(req);

        if (name == null) {
            return MISSING_NAME;
        }
        name = name.trim();
        if (name.length() > Constants.MAX_DGS_LISTING_NAME_LENGTH) {
            return INCORRECT_DGS_LISTING_NAME;
        }

        if (description.length() > Constants.MAX_DGS_LISTING_DESCRIPTION_LENGTH) {
            return INCORRECT_DGS_LISTING_DESCRIPTION;
        }

        if (tags.length() > Constants.MAX_DGS_LISTING_TAGS_LENGTH) {
            return INCORRECT_DGS_LISTING_TAGS;
        }

        Appendix.PrunablePlainMessage prunablePlainMessage = (Appendix.PrunablePlainMessage)ParameterParser.getPlainMessage(req, true);
        if (prunablePlainMessage != null) {
            if (prunablePlainMessage.isText()) {
                return MESSAGE_NOT_BINARY;
            }
            byte[] image = prunablePlainMessage.getMessage();
            Tika tika = new Tika();
            String mediaTypeName = tika.detect(image);
            MediaType mediaType = MediaType.parse(mediaTypeName);
            if (mediaType == null || !mediaType.getType().equals("image")) {
                return MESSAGE_NOT_IMAGE;
            }
        }

        Account account = ParameterParser.getSenderAccount(req);
        Attachment attachment = new Attachment.DigitalGoodsListing(name, description, tags, quantity, priceNQT);
        return createTransaction(req, account, attachment);

    }

    private static final JSONStreamAware MESSAGE_NOT_BINARY;
    static {
        JSONObject response = new JSONObject();
        response.put("errorCode", 8);
        response.put("errorDescription", "Only binary message attachments accepted as DGS listing images");
        MESSAGE_NOT_BINARY = JSON.prepare(response);
    }

    private static final JSONStreamAware MESSAGE_NOT_IMAGE;
    static {
        JSONObject response = new JSONObject();
        response.put("errorCode", 9);
        response.put("errorDescription", "Message attachment is not an image");
        MESSAGE_NOT_IMAGE = JSON.prepare(response);
    }

}
