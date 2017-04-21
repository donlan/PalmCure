/*
 *   Copyright 2016, donlan(梁桂栋)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   Email me: stonelavender@hotmail.com
 */

package dong.lan.base.ui.base;

/**
 */

public final class Config {

    public static final String INTENT_USER = "userSeq";

    private Config() {
        //no instance
    }


    public static final int CONTRACT_STATUS_ADD = 0;
    public static final int CONTRACT_STATUS_VERIFY = 1;
    public static final int CONTRACT_STATUS_REJECT = -1;

    public static final int TYPE_DOCTOR = 1;
    public static final int TYPE_PATIENT = 2;
    public static final int APPOINTMENT_ADD = 0;

    public static final int APPOINTMENT_WAIT = 1;

    public static final int APPOINTMENT_FINISH = 2;

    public static final int APPOINTMENT_REJECT = 3;
}
