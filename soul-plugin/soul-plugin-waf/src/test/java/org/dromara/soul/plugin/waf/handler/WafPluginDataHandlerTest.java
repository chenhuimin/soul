/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.soul.plugin.waf.handler;

import org.dromara.soul.common.dto.PluginData;
import org.dromara.soul.common.dto.RuleData;
import org.dromara.soul.common.dto.convert.WafHandle;
import org.dromara.soul.common.enums.PluginEnum;
import org.dromara.soul.common.utils.GsonUtils;
import org.dromara.soul.plugin.base.utils.Singleton;
import org.dromara.soul.plugin.waf.cache.WafRuleHandleCache;
import org.dromara.soul.plugin.waf.config.WafConfig;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test case for {@link WafPluginDataHandler}.
 *
 * @author HoldDie
 */
public final class WafPluginDataHandlerTest {

    private WafPluginDataHandler wafPluginDataHandlerUnderTest;

    @Before
    public void setUp() {
        wafPluginDataHandlerUnderTest = new WafPluginDataHandler();
    }

    @Test
    public void testHandlerPlugin() {
        final PluginData pluginData = new PluginData("pluginId", "pluginName", "{}", 0, false);
        wafPluginDataHandlerUnderTest.handlerPlugin(pluginData);
        WafConfig wafConfig = Singleton.INST.get(WafConfig.class);
        assertEquals(GsonUtils.getInstance().toJson(wafConfig), pluginData.getConfig());
    }

    @Test
    public void testHandlerRule() {
        RuleData ruleData = new RuleData();
        ruleData.setId("wafRule");
        ruleData.setSelectorId("waf");
        WafHandle wafHandle = new WafHandle();
        wafHandle.setPermission("false");
        wafHandle.setStatusCode("0");
        ruleData.setHandle(GsonUtils.getGson().toJson(wafHandle));
        wafPluginDataHandlerUnderTest.handlerRule(ruleData);
        assertEquals(wafHandle.getPermission(), WafRuleHandleCache.getInstance().obtainHandle(WafPluginDataHandler.getCacheKeyName(ruleData)).getPermission());
        wafPluginDataHandlerUnderTest.removeRule(ruleData);
        assertNull(WafRuleHandleCache.getInstance().obtainHandle(WafPluginDataHandler.getCacheKeyName(ruleData)));
    }

    @Test
    public void testPluginNamed() {
        final String result = wafPluginDataHandlerUnderTest.pluginNamed();
        assertEquals(PluginEnum.WAF.getName(), result);
    }
}
