package com.mobis.cp.client.constant

/**
 * REQ : client requests to server
 * RESP : server responses to client
 * NOTIFICATION : server broadcasts to client
 * @property command
 */
enum class MsgCommand(val command: Int) {
    /**
     * Connection 요청 하여 서버 VR 에서 사용할 client messenger binder 전달
     *
     */
    MSG_REQ_CONNECTION(0),

    /**
     * VR 에서 PTT 눌러서 음성인식 상황 조건에 만족하여 리스닝 상태 진입시 보내는 메시지
     *
     */
    MSG_NOTIFICATION_VR_START(1),

    /**
     * VR 에서 PTT 누르거나 응답 종료 시 슬리핑 상태 진입시 보내는 메시지
     *
     */
    MSG_NOTIFICATION_VR_STOP(2),

    /**
     * 모비스에서 VR 상태 요청시 메시지
     *
     */
    MSG_REQ_VR_STATUS(3),

    /**
     * 모비스에서 VR 상태 요청 메시지에 대한 VR 응답
     *
     */
    MSG_RESP_VR_STATUS(4)
}