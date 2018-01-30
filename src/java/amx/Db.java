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

package amx;

import amx.db.BasicDb;
import amx.db.TransactionalDb;

public final class Db {

    public static final String PREFIX = Constants.isTestnet ? "amx.testDb" : "amx.db";
    public static final TransactionalDb db = new TransactionalDb(new BasicDb.DbProperties()
            .maxCacheSize(Amx.getIntProperty("amx.dbCacheKB"))
            .dbUrl(Amx.getStringProperty(PREFIX + "Url"))
            .dbType(Amx.getStringProperty(PREFIX + "Type"))
            .dbDir(Amx.getStringProperty(PREFIX + "Dir"))
            .dbParams(Amx.getStringProperty(PREFIX + "Params"))
            .dbUsername(Amx.getStringProperty(PREFIX + "Username"))
            .dbPassword(Amx.getStringProperty(PREFIX + "Password", null, true))
            .maxConnections(Amx.getIntProperty("amx.maxDbConnections"))
            .loginTimeout(Amx.getIntProperty("amx.dbLoginTimeout"))
            .defaultLockTimeout(Amx.getIntProperty("amx.dbDefaultLockTimeout") * 1000)
            .maxMemoryRows(Amx.getIntProperty("amx.dbMaxMemoryRows"))
    );

    static void init() {
        db.init(new AmxDbVersion());
    }

    static void shutdown() {
        db.shutdown();
    }

    private Db() {} // never

}
