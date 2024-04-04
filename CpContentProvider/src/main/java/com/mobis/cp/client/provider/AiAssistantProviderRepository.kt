package com.mobis.cp.client.provider

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiAssistantProviderRepository @Inject constructor(
    private val aiAssistantProvider: AiAssistantProvider
) {
    fun getAgentStatus() = aiAssistantProvider.getAgentStatus()
    fun callAgentStatus() = aiAssistantProvider.callAgentStatus()
}