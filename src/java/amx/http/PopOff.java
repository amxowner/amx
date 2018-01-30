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

import amx.Block;
import amx.Amx;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public final class PopOff extends APIServlet.APIRequestHandler {

    static final PopOff instance = new PopOff();

    private PopOff() {
        super(new APITag[] {APITag.DEBUG}, "numBlocks", "height", "keepTransactions");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) {

        int numBlocks = 0;
        try {
            numBlocks = Integer.parseInt(req.getParameter("numBlocks"));
        } catch (NumberFormatException ignored) {}
        int height = 0;
        try {
            height = Integer.parseInt(req.getParameter("height"));
        } catch (NumberFormatException ignored) {}
        boolean keepTransactions = "true".equalsIgnoreCase(req.getParameter("keepTransactions"));
        List<? extends Block> blocks;
        try {
            Amx.getBlockchainProcessor().setGetMoreBlocks(false);
            if (numBlocks > 0) {
                blocks = Amx.getBlockchainProcessor().popOffTo(Amx.getBlockchain().getHeight() - numBlocks);
            } else if (height > 0) {
                blocks = Amx.getBlockchainProcessor().popOffTo(height);
            } else {
                return JSONResponses.missing("numBlocks", "height");
            }
        } finally {
            Amx.getBlockchainProcessor().setGetMoreBlocks(true);
        }
        JSONArray blocksJSON = new JSONArray();
        blocks.forEach(block -> blocksJSON.add(JSONData.block(block, true, false)));
        JSONObject response = new JSONObject();
        response.put("blocks", blocksJSON);
        if (keepTransactions) {
            blocks.forEach(block -> Amx.getTransactionProcessor().processLater(block.getTransactions()));
        }
        return response;
    }

    @Override
    protected final boolean requirePost() {
        return true;
    }

    @Override
    protected boolean requirePassword() {
        return true;
    }

    @Override
    protected final boolean allowRequiredBlockParameters() {
        return false;
    }

    @Override
    protected boolean requireBlockchain() {
        return false;
    }

}
