package com.cplier.diameter

import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.experimental.and
import kotlin.experimental.or


/* Diameter Message Header Flags */
const val HEADER_FLAG_NONE                                      = 0x00.toByte()
const val HEADER_FLAG_E                                         = 0x20.toByte()
const val HEADER_FLAG_E_T                                       = 0x30.toByte()
const val HEADER_FLAG_P                                         = 0x40.toByte()
const val HEADER_FLAG_P_T                                       = 0x50.toByte()
const val HEADER_FLAG_P_E                                       = 0x60.toByte()
const val HEADER_FLAG_R                                         = 0x80.toByte()
const val HEADER_FLAG_R_T                                       = 0x90.toByte()
const val HEADER_FLAG_R_P                                       = 0xC0.toByte()
const val HEADER_FLAG_R_P_T                                     = 0xD0.toByte()

/* Diameter Message Header Flag Bits Masks */
const val HEADER_MASK_BIT_R                                     = 0x80.toByte()
const val HEADER_MASK_BIT_P                                     = 0x40.toByte()
const val HEADER_MASK_BIT_E                                     = 0x20.toByte()
const val HEADER_MASK_BIT_T                                     = 0x10.toByte()
const val HEADER_MASK_RESERVED                                  = 0x0F.toByte()

/* AVP Flags */
const val AVP_FLAG_V                                            = 0x80.toByte()
const val AVP_FLAG_M                                            = 0x40.toByte()
const val AVP_FLAG_V_M                                          = 0xC0.toByte()
const val AVP_FLAG_NONE                                         = 0x00.toByte()


/* Diameter AVP Flag Bits Masks */
const val AVP_MASK_BIT_V                                        = 0x80.toByte()
const val AVP_MASK_BIT_M                                        = 0x40.toByte()
const val AVP_MASK_BIT_P                                        = 0x20.toByte()
const val AVP_MASK_RESERVED                                     = 0x1F.toByte()

/* Diameter Message Header Length Definitions */
const val DIAMETER_MSG_HDR_LEN                                  = 20

/* Diameter AVP Header Length(bytes) Definitions */
const val AVP_HDR_LEN_WITHOUT_VENDOR                            = 8
const val AVP_HDR_LEN_WITH_VENDOR                               = AVP_HDR_LEN_WITHOUT_VENDOR + 4

/* Available Diameter Versions */
const val DIAMETER_VERSION                                      = 1.toByte()

/* InetAddress Types */
const val ADDRESS_TYPE_IPv4                                     = 1.toShort()
const val ADDRESS_TYPE_IPv6                                     = 2.toShort()

/* AVP Data Types - DT stands for "Data Type" */
const val DT_UNKNOWN                                            = -1
const val DT_OCTET_STRING                                       = 0
const val DT_INTEGER_32                                         = 1
const val DT_INTEGER_64                                         = 2
const val DT_UNSIGNED_32                                        = 3
const val DT_UNSIGNED_64                                        = 4
const val DT_FLOAT_32                                           = 5
const val DT_FLOAT_64                                           = 6
const val DT_GROUPED                                            = 7
const val DT_ADDRESS                                            = 8
const val DT_TIME                                               = 9
const val DT_UTF8STRING                                         = 10
const val DT_DIAMETER_IDENTITY                                  = 11
const val DT_DIAMETER_URI                                       = 12
const val DT_ENUMERATED                                         = 13
const val DT_IP_FILTER_RULE                                     = 14
const val DT_QoS_FILTER_RULE                                    = 15

/* Command Codes */
const val COMMAND_AAR_AAA                                       = 265                 /* [RFC7155] */
const val COMMAND_CER_CEA                                       = 257                 /* [RFC6733] */
const val COMMAND_RAR_RAA                                       = 258                 /* [RFC6733] */
const val COMMAND_AMR_AMA                                       = 260                 /* [RFC4004] */
const val COMMAND_HAR_HAA                                       = 262                 /* [RFC4004] */
const val COMMAND_DER_DEA                                       = 268                 /* [RFC4072] */
const val COMMAND_ACR_ACA                                       = 271                 /* [RFC6733] */
const val COMMAND_CCR_CCA                                       = 272                 /* [RFC4006] */
const val COMMAND_ASR_ASA                                       = 274                 /* [RFC6733] */
const val COMMAND_STR_STA                                       = 275                 /* [RFC6733] */
const val COMMAND_DWR_DWA                                       = 280                 /* [RFC6733] */
const val COMMAND_DPR_DPA                                       = 282                 /* [RFC6733] */
const val COMMAND_UAR_UAA                                       = 283                 /* [RFC4740] */
const val COMMAND_SAR_SAA                                       = 284                 /* [RFC4740] */
const val COMMAND_LIR_LIA                                       = 285                 /* [RFC4740] */
const val COMMAND_MAR_MAA                                       = 286                 /* [RFC4740] */
const val COMMAND_RTR_RTA                                       = 287                 /* [RFC4740] */
const val COMMAND_PPR_PPA                                       = 288                 /* [RFC4740] */
const val COMMAND_PDR_PDA                                       = 314                 /* [RFC5224] */
const val COMMAND_SIR_SIA                                       = 8388641             /* [RFC5234] */
const val COMMAND_CIR_CIA                                       = 8388718             /* [RFC5234] */
const val COMMAND_RIR_RIA                                       = 8388719             /* [RFC5234] */
const val COMMAND_NIR_NIA                                       = 8388726             /* [RFC5234] */

/* AVP Codes - AC stands for "AVP Code" */
const val AC_MIP_FA_TO_HA_SPI                                   = 318L                /* [RFC4004] */
const val AC_MIP_FA_TO_MN_SPI                                   = 319L                /* [RFC4004] */
const val AC_MIP_REG_REQUEST                                    = 320L                /* [RFC4004] */
const val AC_MIP_REG_REPLY                                      = 321L                /* [RFC4004] */
const val AC_MIP_MN_AAA_AUTH                                    = 322L                /* [RFC4004] */
const val AC_MIP_HA_TO_FA_SPI                                   = 323L                /* [RFC4004] */
const val AC_MIP_MN_TO_FA_MSA                                   = 325L                /* [RFC4004] */
const val AC_MIP_FA_TO_MN_MSA                                   = 326L                /* [RFC4004] */
const val AC_MIP_FA_TO_HA_MSA                                   = 328L                /* [RFC4004] */
const val AC_MIP_HA_TO_FA_MSA                                   = 329L                /* [RFC4004] */
const val AC_MIP_MN_TO_HA_MSA                                   = 331L                /* [RFC4004] */
const val AC_MIP_HA_TO_MN_MSA                                   = 332L                /* [RFC4004] */
const val AC_MIP_MOBILE_NODE_ADRESS                             = 333L                /* [RFC4004] */
const val AC_MIP_NONCE                                          = 335L                /* [RFC4004] */
const val AC_MIP_CANDIDATE_HOME_AGENT_HOST                      = 336L                /* [RFC4004] */
const val AC_MIP_FEATURE_VECTOR                                 = 337L                /* [RFC4004] */
const val AC_MIP_AUTH_INPUT_DATA_LENGTH                         = 338L                /* [RFC4004] */
const val AC_MIP_AUTHENTICATOR_LENGTH                           = 339L                /* [RFC4004] */
const val AC_MIP_AUTHENTICATOR_OFFSET                           = 340L                /* [RFC4004] */
const val AC_MIP_MN_AAA_SPI                                     = 341L                /* [RFC4004] */
const val AC_MIP_FILTER_RULE                                    = 342L                /* [RFC4004] */
const val AC_MIP_SESSION_KEY                                    = 343L                /* [RFC4004] */
const val AC_MIP_FA_CHALLENGE                                   = 344L                /* [RFC4004] */
const val AC_MIP_ALGORITHM_TYPE                                 = 345L                /* [RFC4004] */
const val AC_MIP_REPLAY_MODE                                    = 346L                /* [RFC4004] */
const val AC_MIP_ORIGINATING_FOREIGN_AAA                        = 347L                /* [RFC4004] */
const val AC_MIP_MSA_LIFETIME                                   = 367L                /* [RFC4004] */
const val AC_SIP_ACCOUNTING_INFORMATION                         = 368L                /* [RFC4740] */
const val AC_SIP_ACCOUNTING_SERVER_URI                          = 369L                /* [RFC4740] */
const val AC_SIP_CREDIT_CONTROL_SERVER_URI                      = 370L                /* [RFC4740] */
const val AC_SIP_SERVER_URI                                     = 371L                /* [RFC4740] */
const val AC_SIP_SERVER_CAPABILITIES                            = 372L                /* [RFC4740] */
const val AC_SIP_MANDATORY_CAPABILITY                           = 373L                /* [RFC4740] */
const val AC_SIP_OPTIONAL_CAPABILITY                            = 374L                /* [RFC4740] */
const val AC_SIP_SERVER_ASSIGNMENT_TYPE                         = 375L                /* [RFC4740] */
const val AC_SIP_AUTHENTICATION_INFO                            = 381L                /* [RFC4740] */
const val AC_SIP_DEREGISTRATION_REASON                          = 383L                /* [RFC4740] */
const val AC_SIP_REASON_CODE                                    = 384L                /* [RFC4740] */
const val AC_SIP_REASON_INFO                                    = 385L                /* [RFC4740] */
const val AC_SIP_VISITED_NETWORK_ID                             = 386L                /* [RFC4740] */
const val AC_SIP_USER_AUTHORIZATION_TYPE                        = 387L                /* [RFC4740] */
const val AC_SIP_SUPPORTED_USER_DATA_TYPE                       = 388L                /* [RFC4740] */
const val AC_SIP_USER_DATA                                      = 389L                /* [RFC4740] */
const val AC_SIP_USER_DATA_TYPE                                 = 390L                /* [RFC4740] */
const val AC_SIP_USER_DATA_CONTENTS                             = 391L                /* [RFC4740] */
const val AC_SIP_USER_DATA_ALREADY_AVAILABLE                    = 392L                /* [RFC4740] */
const val AC_CHAP_ALGORTIHM                                     = 403L                /* [RFC7155] */
const val AC_QoS_FILTER_RULE                                    = 407L                /* [RFC7155] */
const val AC_REQUEST_ACTION                                     = 436L                /* [RFC4006] */
const val AC_REQUEST_SERVICE_UNIT                               = 437L                /* [RFC4006] */
const val AC_MIP_AUTHENTICATOR                                  = 488L                /* [RFC5778] */
const val AC_MIP_MAC_MOBILITY_DATA                              = 489L                /* [RFC5778] */
const val AC_MIP_TIMESTAMP                                      = 490L                /* [RFC5778] */
const val AC_MIP_MN_HA_SPI                                      = 491L                /* [RFC5778] */
const val AC_MIP_MN_HA_MSA                                      = 492L                /* [RFC5778] */
const val AC_MIP6_AUTH_MODE                                     = 494L                /* [RFC5778] */
const val AC_TMOD_1                                             = 495L                /* [RFC5624] */
const val AC_TOKEN_RATE                                         = 496L                /* [RFC5624] */
const val AC_BUCKET_DEPTH                                       = 497L                /* [RFC5624] */
const val AC_PEAK_TRAFFIC_RATE                                  = 498L                /* [RFC5624] */
const val AC_MINIMUM_POLICED_UNIT                               = 499L                /* [RFC5624] */
const val AC_MAXIMUM_PACKET_SIZE                                = 500L                /* [RFC5624] */
const val AC_TMOD_2                                             = 501L                /* [RFC5624] */
const val AC_BANDWITH                                           = 502L                /* [RFC5624] */
const val AC_PHB_CLASS                                          = 503L                /* [RFC5624] */
const val AC_PMIP6_DHCP_SERVER_ADDRESS                          = 504L                /* [RFC5779] */
const val AC_PMIP6_IPV4_HOME_ADDRESS                            = 505L                /* [RFC5779] */
const val AC_SERVICE_CONFIGURATION                              = 507L                /* [RFC5779] */
const val AC_QoS_RESOURCES                                      = 508L                /* [RFC5777] */
const val AC_IP_MASK_BIT_MASK_WIDTH                             = 523L                /* [RFC5777] */
const val AC_QoS_PROFILE_ID                                     = 573L                /* [RFC5777] */
const val AC_QoS_PROFILE_TEMPLATE                               = 574L                /* [RFC5777] */
const val AC_QoS_SEMANTICS                                      = 575L                /* [RFC5777] */
const val AC_QoS_PARAMETERS                                     = 576L                /* [RFC5777] */
const val AC_QoS_CAPABILITY                                     = 578L                /* [RFC5777] */
const val AC_QoS_AUTHORIZATION_DATA                             = 579L                /* [RFC5866] */
const val AC_BOUND_AUTH_SESSION_ID                              = 580L                /* [RFC5866] */
const val AC_IKEv2_NONCES                                       = 587L                /* [RFC6738] */
const val AC_NI                                                 = 588L                /* [RFC6738] */
const val AC_NR                                                 = 589L                /* [RFC6738] */
const val AC_IKEv2_IDENTITY                                     = 590L                /* [RFC6738] */
const val AC_INITIATOR_IDENTITY                                 = 591L                /* [RFC6738] */
const val AC_ID_TYPE                                            = 592L                /* [RFC6738] */
const val AC_IDENTIFICATION_DATA                                = 593L                /* [RFC6738] */
const val AC_RESPONDER_IDENTITY                                 = 594L                /* [RFC6738] */
const val AC_NC_REQUEST_TYPE                                    = 595L                /* [RFC6736] */
const val AC_NAT_CONTROL_INSTALL                                = 596L                /* [RFC6736] */
const val AC_NAT_CONTROL_REMOVE                                 = 597L                /* [RFC6736] */
const val AC_NAT_CONTROL_DEFINITION                             = 598L                /* [RFC6736] */
const val AC_NAT_INTERNAL_ADDRESS                               = 599L                /* [RFC6736] */
const val AC_NAT_EXTERNAL_ADDRESS                               = 600L                /* [RFC6736] */
const val AC_MAX_NAT_BINDINGS                                   = 601L                /* [RFC6736] */
const val AC_NAT_CONTROL_BINDING_TEMPLATE                       = 602L                /* [RFC6736] */
const val AC_DUPLICATE_SESSION_ID                               = 603L                /* [RFC6736] */
const val AC_NAT_EXTERNAL_PORT_STYLE                            = 604L                /* [RFC6736] */
const val AC_NAT_CONTROL_RECORD                                 = 605L                /* [RFC6736] */
const val AC_NAT_CONTROL_BINDING_STATUS                         = 606L                /* [RFC6736] */
const val AC_CURRENT_NAT_BINDINGS                               = 607L                /* [RFC6736] */
const val AC_DUAL_PRIORITY                                      = 608L                /* [RFC6735] */
const val AC_PREEMPTION_PRIORITY                                = 609L                /* [RFC6735] */
const val AC_DEFENDING_PRIORITY                                 = 610L                /* [RFC6735] */
const val AC_ADMISSION_PRIORITY                                 = 611L                /* [RFC6735] */
const val AC_SIP_RESOURCE_PRIORITY                              = 612L                /* [RFC6735] */
const val AC_SIP_RESOURCE_PRIORITY_NAMESPACE                    = 613L                /* [RFC6735] */
const val AC_SIP_RESOURCE_PRIORITY_VALUE                        = 614L                /* [RFC6735] */
const val AC_APPLICATION_LEVEL_RESOURCE_PRIORITY                = 615L                /* [RFC6735] */
const val AC_ALRP_NAMESPACE                                     = 616L                /* [RFC6735] */
const val AC_ALRP_VALUE                                         = 617L                /* [RFC6735] */
const val AC_REDIRECT_REALM                                     = 620L                /* [RFC7075] */
const val AC_ECN_IP_CODEPOINT                                   = 628L                /* [RFC7660] */
const val AC_CONGESTION_TREATMENT                               = 629L                /* [RFC7660] */
const val AC_FLOW_COUNT                                         = 630L                /* [RFC7660] */
const val AC_PACKET_COUNT                                       = 631L                /* [RFC7660] */
const val AC_IP_PREFIX_LENGTH                                   = 632L                /* [RFC7678] */
const val AC_BORDER_ROUTER_NAME                                 = 633L                /* [RFC7678] */
const val AC_64_MULTICAST_ATTRIBUTES                            = 634L                /* [RFC7678] */
const val AC_ASM_MPREFIX64                                      = 635L                /* [RFC7678] */
const val AC_SSM_MPREFIX64                                      = 636L                /* [RFC7678] */
const val AC_TUNNEL_SOURCE_PREF_OR_ADDR                         = 637L                /* [RFC7678] */
const val AC_TUNNEL_SOURCE_IPv6_ADDRESS                         = 638L                /* [RFC7678] */
const val AC_PORT_SET_IDENTIFIER                                = 639L                /* [RFC7678] */
const val AC_LW4O6_BINDING                                      = 640L                /* [RFC7678] */
const val AC_LW4O6_EXTERNAL_IPv4_ADDR                           = 641L                /* [RFC7678] */
const val AC_MAP_E_ATTRIBUTES                                   = 642L                /* [RFC7678] */
const val AC_MAP_MESH_MODE                                      = 643L                /* [RFC7678] */
const val AC_MAP_MAPPING_RULE                                   = 644L                /* [RFC7678] */
const val AC_RULE_IPv4_ADDR_OR_PREFIX                           = 645L                /* [RFC7678] */
const val AC_RULE_IPv6_PREFIX                                   = 646L                /* [RFC7678] */
const val AC_EA_FIELD_LENGTH                                    = 647L                /* [RFC7678] */
const val AC_OC_PEER_ALGO                                       = 648L                /* [DimeAgentOverload11] */
const val AC_SOURCEID                                           = 649L                /* [DimeAgentOverload11] */
const val AC_LOAD                                               = 650L                /* [RFCIetfDimeLoad09] */
const val AC_LOAD_TYPE                                          = 651L                /* [RFCIetfDimeLoad09] */
const val AC_LOAD_VALUE                                         = 652L                /* [RFCIetfDimeLoad09] */
const val AC_1XRTT_RCID                                         = 2554L               /* [29.172-d10] */
const val AC_3GPP_AAA_SERVER_NAME                               = 318L                /* [29.234-b20] */
const val AC_3GPP_ALLOCATE_IP_TYPE                              = 27L                 /* [29.061-f10] */
const val AC_3GPP_CAMEL_CHARGING_INFO                           = 24L                 /* [29.061-f10] */
const val AC_3GPP_CHARGING_CHARACTERISTICS                      = 13L                 /* [29.061-f10] */
const val AC_3GPP_CHARGING_DNS_SERVERS                          = 17L                 /* [29.061-f10] */
const val AC_3GPP_CHARGING_GATEWAY_ADDRESS                      = 4L                  /* [29.061-f10] */
const val AC_3GPP_CHARGING_GATEWAY_IPV6_ADDRESS                 = 14L                 /* [29.061-f10] */
const val AC_3GPP_CHARGING_ID                                   = 2L                  /* [29.061-f10] */
const val AC_3GPP_GGSN_ADDRESS                                  = 7L                  /* [29.061-f10] */
const val AC_3GPP_GGSN_IPV6_ADDRESS                             = 16L                 /* [29.061-f10] */
const val AC_3GPP_GGSN_MCC_MNC                                  = 9L                  /* [29.061-f10] */
const val AC_3GPP_GPRS_NEGOTIATED_QOS_PROFILE                   = 5L                  /* [29.061-f10] */
const val AC_3GPP_IMEISV                                        = 20L                 /* [29.061-f10] */
const val AC_3GPP_IMSI                                          = 1L                  /* [29.061-f10] */
const val AC_3GPP_IMSI_MCC_MNC                                  = 8L                  /* [29.061-f10] */
const val AC_3GPP_MS_TIMEZONE                                   = 23L                 /* [29.061-f10] */
const val AC_3GPP_NEGOTIATED_DSCP                               = 26L                 /* [29.061-f10] */
const val AC_3GPP_NSAPI                                         = 10L                 /* [29.061-f10] */
const val AC_3GPP_PACKET_FILTER                                 = 25L                 /* [29.061-f10] */
const val AC_3GPP_PDP_TYPE                                      = 3L                  /* [29.061-f10] */
const val AC_3GPP_PS_DATA_OFF_STATUS                            = 2847L               /* [29.212-f10] */
const val AC_3GPP_RAT_TYPE                                      = 21L                 /* [29.061-f10] */
const val AC_3GPP_SELECTION_MODE                                = 12L                 /* [29.061-f10] */
const val AC_3GPP_SESSION_STOP_INDICATOR                        = 11L                 /* [29.061-f10] */
const val AC_3GPP_SGSN_ADDRESS                                  = 6L                  /* [29.061-f10] */
const val AC_3GPP_SGSN_IPV6_ADDRESS                             = 15L                 /* [29.061-f10] */
const val AC_3GPP_SGSN_MCC_MNC                                  = 18L                 /* [29.061-f10] */
const val AC_3GPP_SGSN_MCC_MNC_GX                               = 18L                 /* [29.061] */
const val AC_3GPP_TEARDOWN_INDICATOR                            = 19L                 /* [29.061-f10] */
const val AC_3GPP_USER_LOCATION_INFO                            = 22L                 /* [29.061-f10] */
const val AC_3GPP_USER_LOCATION_INFO_TIME                       = 30L                 /* [29.061-f10] */
const val AC_3GPP_WLAN_APN_ID                                   = 100L                /* [29.003] */
const val AC_3GPP2_BSID                                         = 9010L               /* [3GPP2 X.S0057-0 v3.0] */
const val AC_3GPP2_MEID                                         = 1471L               /* [29.272-f10] */
const val AC_A_MSISDN                                           = 1643L               /* [29.272-f10] */
const val AC_AAA_FAILURE_INDICATION                             = 1518L               /* [29.273-f10] */
const val AC_AAR_FLAGS                                          = 1539L               /* [29.273-f10] */
const val AC_ABORT_CAUSE                                        = 500L                /* [29.214-f10] */
const val AC_ABSENT_SUBSCRIBER_DIAGNOSTIC_T4                    = 3201L               /* [29.337-e10] */
const val AC_ABSENT_USER_DIAGNOSTIC_SM                          = 3322L               /* [29.338-f00] */
const val AC_ABSOLUTE_END_FRACTIONAL_SECONDS                    = 569L                /* [RFC 5777] */
const val AC_ABSOLUTE_END_TIME                                  = 568L                /* [RFC 5777] */
const val AC_ABSOLUTE_START_FRACTIONAL_SECONDS                  = 567L                /* [RFC 5777] */
const val AC_ABSOLUTE_START_TIME                                = 566L                /* [RFC 5777] */
const val AC_ACCEPTABLE_SERVICE_INFO                            = 526L                /* [29.214-f10] */
const val AC_ACCESS_AUTHORIZATION_FLAGS                         = 1511L               /* [29.273-f10] */
const val AC_ACCESS_AVAILABILITY_CHANGE_REASON                  = 2833L               /* [29.212-f10] */
const val AC_ACCESS_NETWORK_CHARGING_ADDRESS                    = 501L                /* [29.214-f10] */
const val AC_ACCESS_NETWORK_CHARGING_IDENTIFIER                 = 502L                /* [29.214-f10] */
const val AC_ACCESS_NETWORK_CHARGING_IDENTIFIER_GX              = 1022L               /* [29.212-f10] */
const val AC_ACCESS_NETWORK_CHARGING_IDENTIFIER_VALUE           = 503L                /* [29.214-f10] */
const val AC_ACCESS_NETWORK_INFO                                = 1526L               /* [29.273-f10] */
const val AC_ACCESS_NETWORK_INFORMATION                         = 1263L               /* [32-299-f10] */
const val AC_ACCESS_RESTRICTION_DATA                            = 1426L               /* [29.272-f10] */
const val AC_ACCOUNT_EXPIRATION                                 = 2309L               /* [32-299-f10] */
const val AC_ACCOUNTING_AUTH_METHOD                             = 406L                /* [RFC 7155] */
const val AC_ACCOUNTING_EAP_AUTH_METHOD                         = 465L                /* [no_reference] */
const val AC_ACCOUNTING_INPUT_OCTETS                            = 363L                /* [RFC 7155] */
const val AC_ACCOUNTING_INPUT_PACKETS                           = 365L                /* [RFC 7155] */
const val AC_ACCOUNTING_OUTPUT_OCTETS                           = 364L                /* [RFC 7155] */
const val AC_ACCOUNTING_OUTPUT_PACKETS                          = 366L                /* [RFC 7155] */
const val AC_ACCOUNTING_REALTIME_REQUIRED                       = 483L                /* [RFC 6733] */
const val AC_ACCOUNTING_RECORD_NUMBER                           = 485L                /* [RFC 6733] */
const val AC_ACCOUNTING_RECORD_TYPE                             = 480L                /* [RFC 6733] */
const val AC_ACCOUNTING_SUB_SESSION_ID                          = 287L                /* [RFC 6733] */
const val AC_ACCT_APPLICATION_ID                                = 259L                /* [RFC 6733] */
const val AC_ACCT_AUTHENTIC                                     = 45L                 /* [RFC 7155] */
const val AC_ACCT_DELAY_TIME                                    = 41L                 /* [RFC 7155] */
const val AC_ACCT_INTERIM_INTERVAL                              = 85L                 /* [RFC 6733] */
const val AC_ACCT_LINK_COUNT                                    = 51L                 /* [RFC 7155] */
const val AC_ACCT_MULTI_SESSION_ID                              = 50L                 /* [RFC 6733] */
const val AC_ACCT_SESSION_ID                                    = 44L                 /* [RFC 6733] */
const val AC_ACCT_SESSION_TIME                                  = 46L                 /* [RFC 7155] */
const val AC_ACCT_TUNNEL_CONNECTION                             = 68L                 /* [RFC 7155] */
const val AC_ACCT_TUNNEL_PACKETS_LOST                           = 86L                 /* [RFC 7155] */
const val AC_ACCUMULATED_COST                                   = 2052L               /* [32-299-f10] */
const val AC_ACCURACY                                           = 3137L               /* [29.336-f10] */
const val AC_ACCURACY_FULFILMENT_INDICATOR                      = 2513L               /* [29.172-d10] */
const val AC_ACTION_TYPE                                        = 3005L               /* [29.368-e20] */
const val AC_ACTIVE_APN                                         = 1612L               /* [29.272-f10] */
const val AC_ACTIVE_TIME                                        = 4324L               /* [29.128-f00] */
const val AC_ADAPTATIONS                                        = 1217L               /* [32-299-f10] */
const val AC_ADC_REVALIDATION_TIME                              = 2801L               /* [29.212-f10] */
const val AC_ADC_RULE_BASE_NAME                                 = 1095L               /* [29.212-f10] */
const val AC_ADC_RULE_DEFINITION                                = 1094L               /* [29.212-f10] */
const val AC_ADC_RULE_INSTALL                                   = 1092L               /* [29.212-f10] */
const val AC_ADC_RULE_NAME                                      = 1096L               /* [29.212-f10] */
const val AC_ADC_RULE_REMOVE                                    = 1093L               /* [29.212-f10] */
const val AC_ADC_RULE_REPORT                                    = 1097L               /* [29.212-f10] */
const val AC_ADDITIONAL_CONTENT_INFORMATION                     = 1207L               /* [32-299-f10] */
const val AC_ADDITIONAL_CONTEXT_IDENTIFIER                      = 1683L               /* [29.272-f10] */
const val AC_ADDITIONAL_EXCEPTION_REPORTS                       = 3936L               /* [32-299-f10] */
const val AC_ADDITIONAL_SERVING_NODE                            = 2406L               /* [29.173-e00] */
const val AC_ADDITIONAL_SERVING_NODE_T4                         = 2406L               /* [29.336-f10] */
const val AC_ADDITIONAL_TYPE_INFORMATION                        = 1205L               /* [32-299-f10] */
const val AC_ADDRESS_DATA                                       = 897L                /* [32-299-f10] */
const val AC_ADDRESS_DOMAIN                                     = 898L                /* [32-299-f10] */
const val AC_ADDRESS_TYPE                                       = 899L                /* [32-299-f10] */
const val AC_ADDRESSEE_TYPE                                     = 1208L               /* [32-299-f10] */
const val AC_ADJACENT_ACCESS_RESTRICTION_DATA                   = 1673L               /* [29.272-f10] */
const val AC_ADJACENT_PLMNS                                     = 1672L               /* [29.272-f10] */
const val AC_AESE_COMMUNICATION_PATTERN                         = 3113L               /* [29.336-f10] */
const val AC_AESE_COMMUNICATION_PATTERN_CONFIG_STATUS           = 3120L               /* [29.336-f10] */
const val AC_AESE_COMMUNICATION_PATTERN_S6                      = 3113L               /* [29.272-f10] */
const val AC_AESE_ERROR_REPORT                                  = 3121L               /* [29.336-f10] */
const val AC_AF_APPLICATION_IDENTIFIER                          = 504L                /* [29.214-f10] */
const val AC_AF_CHARGING_IDENTIFIER                             = 505L                /* [29.214-f10] */
const val AC_AF_CORRELATION_INFORMATION                         = 1276L               /* [32-299-f10] */
const val AC_AF_REQUESTED_DATA                                  = 551L                /* [29.214-f10] */
const val AC_AF_SIGNALLING_PROTOCOL                             = 529L                /* [29.214-f10] */
const val AC_AGE_OF_LOCATION_ESTIMATE                           = 2514L               /* [29.172-d10] */
const val AC_AGE_OF_LOCATION_INFORMATION                        = 1611L               /* [29.272-f10] */
const val AC_AGGREGATED_CONGESTION_INFO                         = 4000L               /* [29.217-e20] */
const val AC_AGGREGATED_RUCI_REPORT                             = 4001L               /* [29.217-e20] */
const val AC_AIR_FLAGS                                          = 1679L               /* [29.272-f10] */
const val AC_ALERT_REASON                                       = 1434L               /* [29.272-f10] */
const val AC_ALL_APN_CONFIGURATIONS_INCLUDED_INDICATOR          = 1428L               /* [29.272-f10] */
const val AC_ALLOCATION_RETENTION_PRIORITY                      = 1034L               /* [29.212-f10] */
const val AC_ALLOWED_PLMN_LIST                                  = 3158L               /* [29.336-f10] */
const val AC_ALLOWED_WAF_WWSF_IDENTITIES                        = 656L                /* [29.229-e20] */
const val AC_ALTERNATE_CHARGED_PARTY_ADDRESS                    = 1280L               /* [32-299-f10] */
const val AC_AMBR                                               = 1435L               /* [29.272-f10] */
const val AC_AN_GW_ADDRESS                                      = 1050L               /* [29.212-f10] */
const val AC_AN_GW_STATUS                                       = 2811L               /* [29.212-f10] */
const val AC_AN_TRUSTED                                         = 1503L               /* [29.273-f10] */
const val AC_ANID                                               = 1504L               /* [29.273-f10] */
const val AC_AOC_COST_INFORMATION                               = 2053L               /* [32-299-f10] */
const val AC_AOC_FORMAT                                         = 2310L               /* [32-299-f10] */
const val AC_AOC_INFORMATION                                    = 2054L               /* [32-299-f10] */
const val AC_AOC_REQUEST_TYPE                                   = 2055L               /* [32-299-f10] */
const val AC_AOC_SERVICE                                        = 2311L               /* [32-299-f10] */
const val AC_AOC_SERVICE_OBLIGATORY_TYPE                        = 2312L               /* [32-299-f10] */
const val AC_AOC_SERVICE_TYPE                                   = 2313L               /* [32-299-f10] */
const val AC_AOC_SUBSCRIPTION_INFORMATION                       = 2314L               /* [32-299-f10] */
const val AC_APN_AGGREGATE_MAX_BITRATE_DL                       = 1040L               /* [29.212-f10] */
const val AC_APN_AGGREGATE_MAX_BITRATE_UL                       = 1041L               /* [29.212-f10] */
const val AC_APN_AUTHORIZED                                     = 307L                /* [29.234-b20] */
const val AC_APN_BARRING_TYPE                                   = 309L                /* [29.234-b20] */
const val AC_APN_CONFIGURATION                                  = 1430L               /* [29.272-f10] */
const val AC_APN_CONFIGURATION_PROFILE                          = 1429L               /* [29.272-f10] */
const val AC_APN_CONFIGURATION_S6                               = 1430L               /* [29.272-f10] */
const val AC_APN_OI_REPLACEMENT                                 = 1427L               /* [29.272-f10] */
const val AC_APN_RATE_CONTROL                                   = 3933L               /* [32-299-f10] */
const val AC_APN_RATE_CONTROL_DOWNLINK                          = 3934L               /* [32-299-f10] */
const val AC_APN_RATE_CONTROL_UPLINK                            = 3935L               /* [32-299-f10] */
const val AC_APN_VALIDITY_TIME                                  = 3169L               /* [29.336-f10] */
const val AC_APPLIC_ID                                          = 1218L               /* [32-299-f10] */
const val AC_APPLICATION_DETECTION_INFORMATION                  = 1098L               /* [29.212-f10] */
const val AC_APPLICATION_PORT_IDENTIFIER                        = 3010L               /* [29.368-e20] */
const val AC_APPLICATION_PROVIDED_CALLED_PARTY_ADDRESS          = 837L                /* [32-299-f10] */
const val AC_APPLICATION_SERVER                                 = 836L                /* [32-299-f10] */
const val AC_APPLICATION_SERVER_ID                              = 2101L               /* [OMADDSChargingData] */
const val AC_APPLICATION_SERVER_INFORMATION                     = 850L                /* [32-299-f10] */
const val AC_APPLICATION_SERVICE_PROVIDER_IDENTITY              = 532L                /* [29.214-f10] */
const val AC_APPLICATION_SERVICE_TYPE                           = 2102L               /* [OMADDSChargingData] */
const val AC_APPLICATION_SESSION_ID                             = 2103L               /* [OMADDSChargingData] */
const val AC_ARAP_CHALLENGE_RESPONSE                            = 84L                 /* [RFC 7155] */
const val AC_ARAP_FEATURES                                      = 71L                 /* [RFC 7155] */
const val AC_ARAP_PASSWORD                                      = 70L                 /* [RFC 7155] */
const val AC_ARAP_SECURITY                                      = 73L                 /* [RFC 7155] */
const val AC_ARAP_SECURITY_DATA                                 = 74L                 /* [RFC 7155] */
const val AC_ARAP_ZONE_ACCESS                                   = 72L                 /* [RFC 7155] */
const val AC_AREA                                               = 2535L               /* [29.172-d10] */
const val AC_AREA_DEFINITION                                    = 2534L               /* [29.172-d10] */
const val AC_AREA_EVENT_INFO                                    = 2533L               /* [29.172-d10] */
const val AC_AREA_IDENTIFICATION                                = 2537L               /* [29.172-d10] */
const val AC_AREA_SCOPE                                         = 1624L               /* [29.272-f10] */
const val AC_AREA_TYPE                                          = 2536L               /* [29.172-d10] */
const val AC_AS_NUMBER                                          = 722L                /* [29.329-f00] */
const val AC_ASSOCIATED_IDENTITIES                              = 632L                /* [29.229-e20] */
const val AC_ASSOCIATED_PARTY_ADDRESS                           = 2035L               /* [32-299-f10] */
const val AC_ASSOCIATED_REGISTERED_IDENTITIES                   = 647L                /* [29.229-e20] */
const val AC_ASSOCIATED_URI                                     = 856L                /* [32-299-f10] */
const val AC_ASSOCIATION_TYPE                                   = 3138L               /* [29.336-f10] */
const val AC_AUTH_APPLICATION_ID                                = 258L                /* [RFC 6733] */
const val AC_AUTH_GRACE_PERIOD                                  = 276L                /* [RFC 6733] */
const val AC_AUTH_REQUEST_TYPE                                  = 274L                /* [RFC 6733] */
const val AC_AUTH_SESSION_STATE                                 = 277L                /* [RFC 6733] */
const val AC_AUTHENTICATION_INFO                                = 1413L               /* [29.272-f10] */
const val AC_AUTHENTICATION_INFORMATION_SIM                     = 301L                /* [29.234-b20] */
const val AC_AUTHENTICATION_METHOD                              = 300L                /* [29.234-b20] */
const val AC_AUTHORIZATION_INFORMATION_SIM                      = 302L                /* [29.234-b20] */
const val AC_AUTHORIZATION_LIFETIME                             = 291L                /* [RFC 6733] */
const val AC_AUTN                                               = 1449L               /* [29.272-f10] */
const val AC_AUX_APPLIC_INFO                                    = 1219L               /* [32-299-f10] */
const val AC_BAROMETRIC_PRESSURE                                = 2557L               /* [29.172-d10] */
const val AC_BASE_TIME_INTERVAL                                 = 1265L               /* [32-299-f10] */
const val AC_BEARER_CONTROL_MODE                                = 1023L               /* [29.212-f10] */
const val AC_BEARER_IDENTIFIER                                  = 1020L               /* [29.212-f10] */
const val AC_BEARER_OPERATION                                   = 1021L               /* [29.212-f10] */
const val AC_BEARER_SERVICE                                     = 854L                /* [32-299-f10] */
const val AC_BEARER_USAGE                                       = 1000L               /* [29.212-f10] */
const val AC_BSSGP_CAUSE                                        = 64309L              /* [29.128-f10] */
const val AC_BSSID                                              = 2716L               /* [32-299-f10] */
const val AC_C_VID_END                                          = 556L                /* [RFC 5777] */
const val AC_C_VID_START                                        = 555L                /* [RFC 5777] */
const val AC_CALL_BARRING_INFO                                  = 1488L               /* [29.272-f10] */
const val AC_CALL_ID_SIP_HEADER                                 = 643L                /* [29.229-e20] */
const val AC_CALL_REFERENCE_INFO                                = 720L                /* [29.329-f00] */
const val AC_CALL_REFERENCE_NUMBER                              = 721L                /* [29.329-f00] */
const val AC_CALLBACK_ID                                        = 20L                 /* [RFC 7155] */
const val AC_CALLBACK_NUMBER                                    = 19L                 /* [RFC 7155] */
const val AC_CALLED_ASSERTED_IDENTITY                           = 1250L               /* [32-299-f10] */
const val AC_CALLED_IDENTITY                                    = 3916L               /* [32-299-f10] */
const val AC_CALLED_IDENTITY_CHANGE                             = 3917L               /* [32-299-f10] */
const val AC_CALLED_PARTY_ADDRESS                               = 832L                /* [32-299-f10] */
const val AC_CALLED_STATION_ID                                  = 30L                 /* [RFC 7155] */
const val AC_CALLING_PARTY_ADDRESS                              = 831L                /* [32-299-f10] */
const val AC_CALLING_STATION_ID                                 = 31L                 /* [RFC 7155] */
const val AC_CANCELLATION_TYPE                                  = 1420L               /* [29.272-f10] */
const val AC_CARRIER_FREQUENCY                                  = 1696L               /* [29.272-f10] */
const val AC_CARRIER_SELECT_ROUTING_INFORMATION                 = 2023L               /* [32-299-f10] */
const val AC_CAUSE_CODE                                         = 861L                /* [32-299-f10] */
const val AC_CAUSE_TYPE                                         = 4301L               /* [29.128-f10] */
const val AC_CC_CORRELATION_ID                                  = 411L                /* [RFC 4006] */
const val AC_CC_INPUT_OCTETS                                    = 412L                /* [RFC 4006] */
const val AC_CC_MONEY                                           = 413L                /* [RFC 4006] */
const val AC_CC_OUTPUT_OCTETS                                   = 414L                /* [RFC 4006] */
const val AC_CC_REQUEST_NUMBER                                  = 415L                /* [RFC 4006] */
const val AC_CC_REQUEST_TYPE                                    = 416L                /* [RFC 4006] */
const val AC_CC_SERVICE_SPECIFIC_UNITS                          = 417L                /* [RFC 4006] */
const val AC_CC_SESSION_FAILOVER                                = 418L                /* [RFC 4006] */
const val AC_CC_SUB_SESSION_ID                                  = 419L                /* [RFC 4006] */
const val AC_CC_TIME                                            = 420L                /* [RFC 4006] */
const val AC_CC_TOTAL_OCTETS                                    = 421L                /* [RFC 4006] */
const val AC_CC_UNIT_TYPE                                       = 454L                /* [RFC 4006] */
const val AC_CELL_GLOBAL_IDENTITY                               = 1604L               /* [29.272-f10] */
const val AC_CELL_PORTION_ID                                    = 2553L               /* [29.172-d10] */
const val AC_CG_ADDRESS                                         = 846L                /* [32-299-f10] */
const val AC_CHANGE_CONDITION                                   = 2037L               /* [32-299-f10] */
const val AC_CHANGE_TIME                                        = 2038L               /* [32-299-f10] */
const val AC_CHAP_ALGORITHM                                     = 403L                /* [RFC 7155] */
const val AC_CHAP_AUTH                                          = 402L                /* [RFC 7155] */
const val AC_CHAP_CHALLENGE                                     = 60L                 /* [RFC 7155] */
const val AC_CHAP_IDENT                                         = 404L                /* [RFC 7155] */
const val AC_CHAP_RESPONSE                                      = 405L                /* [RFC 7155] */
const val AC_CHARGE_REASON_CODE                                 = 2118L               /* [32-299-f10] */
const val AC_CHARGED_PARTY                                      = 857L                /* [32-299-f10] */
const val AC_CHARGED_PARTY_S6                                   = 857L                /* [29.272-f10] */
const val AC_CHARGING_CHARACTERISTICS                           = 314L                /* [29.234-b20] */
const val AC_CHARGING_CHARACTERISTICS_SELECTION_MODE            = 2066L               /* [32-299-f10] */
const val AC_CHARGING_CORRELATION_INDICATOR                     = 1073L               /* [29.212-f10] */
const val AC_CHARGING_DATA                                      = 304L                /* [29.234-b20] */
const val AC_CHARGING_INFORMATION                               = 618L                /* [29.229-e20] */
const val AC_CHARGING_NODES                                     = 315L                /* [29.234-b20] */
const val AC_CHARGING_PER_IP_CAN_SESSION_INDICATOR              = 4400L               /* [32-299-f10] */
const val AC_CHARGING_RULE_BASE_NAME                            = 1004L               /* [29.212-f10] */
const val AC_CHARGING_RULE_DEFINITION                           = 1003L               /* [29.212-f10] */
const val AC_CHARGING_RULE_INSTALL                              = 1001L               /* [29.212-f10] */
const val AC_CHARGING_RULE_NAME                                 = 1005L               /* [29.212-f10] */
const val AC_CHARGING_RULE_REMOVE                               = 1002L               /* [29.212-f10] */
const val AC_CHARGING_RULE_REPORT                               = 1018L               /* [29.212-f10] */
const val AC_CHECK_BALANCE_RESULT                               = 422L                /* [RFC 4006] */
const val AC_CIA_FLAGS                                          = 3164L               /* [29.336-f10] */
const val AC_CIR_FLAGS                                          = 3145L               /* [29.336-f10] */
const val AC_CIVIC_ADDRESS                                      = 2556L               /* [29.172-d10] */
const val AC_CLASS                                              = 25L                 /* [RFC 6733] */
const val AC_CLASS_IDENTIFIER                                   = 1214L               /* [32-299-f10] */
const val AC_CLASSIFIER                                         = 511L                /* [RFC 5777] */
const val AC_CLASSIFIER_ID                                      = 512L                /* [RFC 5777] */
const val AC_CLIENT_ADDRESS                                     = 2018L               /* [32-299-f10] */
const val AC_CLIENT_IDENTITY                                    = 1480L               /* [29.272-f10] */
const val AC_CLR_FLAGS                                          = 1638L               /* [29.272-f10] */
const val AC_CMR_FLAGS                                          = 4317L               /* [29.128-f10] */
const val AC_CN_IP_MULTICAST_DISTRIBUTION                       = 921L                /* [29.061-f10] */
const val AC_CN_OPERATOR_SELECTION_ENTITY                       = 3421L               /* [32-299-f10] */
const val AC_COA_INFORMATION                                    = 1039L               /* [29.212-f10] */
const val AC_COA_IP_ADDRESS                                     = 1035L               /* [29.212-f10] */
const val AC_CODEC_DATA                                         = 524L                /* [29.214-f10] */
const val AC_COLLECTION_PERIOD_RRM_LTE                          = 1657L               /* [29.272-f10] */
const val AC_COLLECTION_PERIOD_RRM_UMTS                         = 1658L               /* [29.272-f10] */
const val AC_COMMUNICATION_DURATION_TIME                        = 3116L               /* [29.336-f10] */
const val AC_COMMUNICATION_FAILURE_INFORMATION                  = 4300L               /* [29.128-f10] */
const val AC_COMMUNICATION_PATTERN_SET                          = 3114L               /* [29.336-f10] */
const val AC_COMMUNICATION_PATTERN_SET_S6                       = 3114L               /* [29.272-f10] */
const val AC_COMPLETE_DATA_LIST_INCLUDED_INDICATOR              = 1468L               /* [29.272-f10] */
const val AC_CONDITIONAL_APN_AGGREGATE_MAX_BITRATE              = 2818L               /* [29.212-f10] */
const val AC_CONDITIONAL_POLICY_INFORMATION                     = 2840L               /* [29.212-f10] */
const val AC_CONDITIONAL_RESTRICTION                            = 4007L               /* [29.217-e20] */
const val AC_CONFIDENTIALITY_KEY                                = 625L                /* [29.229-e20] */
const val AC_CONFIGURATION_TOKEN                                = 78L                 /* [RFC 7155] */
const val AC_CONGESTION_LEVEL_DEFINITION                        = 4002L               /* [29.217-e20] */
const val AC_CONGESTION_LEVEL_RANGE                             = 4003L               /* [29.217-e20] */
const val AC_CONGESTION_LEVEL_SET_ID                            = 4004L               /* [29.217-e20] */
const val AC_CONGESTION_LEVEL_VALUE                             = 4005L               /* [29.217-e20] */
const val AC_CONGESTION_LOCATION_ID                             = 4006L               /* [29.217-e20] */
const val AC_CONNECT_INFO                                       = 77L                 /* [RFC 7155] */
const val AC_CONNECTION_ACTION                                  = 4314L               /* [29.128-f10] */
const val AC_CONNECTIVITY_FLAGS                                 = 1529L               /* [29.273-f10] */
const val AC_CONTACT                                            = 641L                /* [29.229-e20] */
const val AC_CONTENT_CLASS                                      = 1220L               /* [32-299-f10] */
const val AC_CONTENT_DISPOSITION                                = 828L                /* [32-299-f10] */
const val AC_CONTENT_ID                                         = 2116L               /* [OMADDSChargingData] */
const val AC_CONTENT_LENGTH                                     = 827L                /* [32-299-f10] */
const val AC_CONTENT_PROVIDER_ID                                = 2117L               /* [OMADDSChargingData] */
const val AC_CONTENT_SIZE                                       = 1206L               /* [32-299-f10] */
const val AC_CONTENT_TYPE                                       = 826L                /* [32-299-f10] */
const val AC_CONTENT_VERSION                                    = 552L                /* [29.214-f10] */
const val AC_CONTEXT_IDENTIFIER                                 = 1423L               /* [29.272-f10] */
const val AC_COST_INFORMATION                                   = 423L                /* [RFC 4006] */
const val AC_COST_UNIT                                          = 424L                /* [RFC 4006] */
const val AC_COUNTER_VALUE                                      = 4319L               /* [29.128-f10] */
const val AC_COUPLED_NODE_DIAMETER_ID                           = 1666L               /* [29.272-f10] */
const val AC_CP_CIOT_EPS_OPTIMISATION_INDICATOR                 = 3930L               /* [32-299-f10] */
const val AC_CREDIT_CONTROL                                     = 426L                /* [RFC 4006] */
const val AC_CREDIT_CONTROL_FAILURE_HANDLING                    = 427L                /* [RFC 4006] */
const val AC_CREDIT_MANAGEMENT_STATUS                           = 1082L               /* [29.212-f10] */
const val AC_CS_SERVICE_QOS_REQUEST_IDENTIFIER                  = 2807L               /* [29.212-f10] */
const val AC_CS_SERVICE_QOS_REQUEST_OPERATION                   = 2808L               /* [29.212-f10] */
const val AC_CS_SERVICE_RESOURCE_FAILURE_CAUSE                  = 2814L               /* [29.212-f10] */
const val AC_CS_SERVICE_RESOURCE_REPORT                         = 2813L               /* [29.212-f10] */
const val AC_CS_SERVICE_RESOURCE_RESULT_OPERATION               = 2815L               /* [29.212-f10] */
const val AC_CSG_ACCESS_MODE                                    = 2317L               /* [32-299-f10] */
const val AC_CSG_ID                                             = 1437L               /* [29.272-f10] */
const val AC_CSG_INFORMATION_REPORTING                          = 1071L               /* [29.212-f10] */
const val AC_CSG_MEMBERSHIP_INDICATION                          = 2318L               /* [32-299-f10] */
const val AC_CSG_SUBSCRIPTION_DATA                              = 1436L               /* [29.272-f10] */
const val AC_CUG_INFORMATION                                    = 2304L               /* [32-299-f10] */
const val AC_CURRENCY_CODE                                      = 425L                /* [RFC 4006] */
const val AC_CURRENT_LOCATION                                   = 707L                /* [29.329-f00] */
const val AC_CURRENT_LOCATION_RETRIEVED                         = 1610L               /* [29.272-f10] */
const val AC_CURRENT_TARIFF                                     = 2056L               /* [32-299-f10] */
const val AC_DATA_CODING_SCHEME                                 = 2001L               /* [32-299-f10] */
const val AC_DATA_REFERENCE                                     = 703L                /* [29.329-f00] */
const val AC_DAY_OF_MONTH_MASK                                  = 564L                /* [RFC 5777] */
const val AC_DAY_OF_WEEK_MASK                                   = 563L                /* [RFC 5777] */
const val AC_DAYLIGHT_SAVING_TIME                               = 1650L               /* [29.272-f10] */
const val AC_DCD_INFORMATION                                    = 2115L               /* [OMADDSChargingData] */
const val AC_DEA_FLAGS                                          = 1521L               /* [29.273-f10] */
const val AC_DEFAULT_ACCESS                                     = 2829L               /* [29.212-f10] */
const val AC_DEFAULT_BEARER_INDICATION                          = 2844L               /* [29.212-f10] */
const val AC_DEFAULT_EPS_BEARER_QOS                             = 1049L               /* [29.212-f10] */
const val AC_DEFAULT_QOS_INFORMATION                            = 2816L               /* [29.212-f10] */
const val AC_DEFAULT_QOS_NAME                                   = 2817L               /* [29.212-f10] */
const val AC_DEFERRED_LOCATION_EVENT_TYPE                       = 1230L               /* [32-299-f10] */
const val AC_DEFERRED_LOCATION_TYPE                             = 2532L               /* [29.172-d10] */
const val AC_DEFERRED_MT_LR_DATA                                = 2547L               /* [29.172-d10] */
const val AC_DELAYED_LOCATION_REPORTING_DATA                    = 2555L               /* [29.172-d10] */
const val AC_DELIVERY_OUTCOME                                   = 3009L               /* [29.368-e20] */
const val AC_DELIVERY_REPORT_REQUESTED                          = 1216L               /* [32-299-f10] */
const val AC_DELIVERY_STATUS                                    = 2104L               /* [OMADDSChargingData] */
const val AC_DER_FLAGS                                          = 1520L               /* [29.273-f10] */
const val AC_DER_S6B_FLAGS                                      = 1523L               /* [29.273-f10] */
const val AC_DEREGISTRATION_REASON                              = 615L                /* [29.229-e20] */
const val AC_DESTINATION_HOST                                   = 293L                /* [RFC 6733] */
const val AC_DESTINATION_INTERFACE                              = 2002L               /* [32-299-f10] */
const val AC_DESTINATION_REALM                                  = 283L                /* [RFC 6733] */
const val AC_DESTINATION_SIP_URI                                = 3327L               /* [29.338-f00] */
const val AC_DEVICE_ACTION                                      = 3001L               /* [29.368-e20] */
const val AC_DEVICE_NOTIFICATION                                = 3002L               /* [29.368-e20] */
const val AC_DIAGNOSTICS                                        = 2039L               /* [32-299-f10] */
const val AC_DIFFSERV_CODE_POINT                                = 535L                /* [RFC 5777] */
const val AC_DIGEST_ALGORITHM                                   = 111L                /* [29.229-e20] */
const val AC_DIGEST_HA1                                         = 121L                /* [29.229-e20] */
const val AC_DIGEST_QOP                                         = 110L                /* [29.229-e20] */
const val AC_DIGEST_REALM                                       = 104L                /* [29.229-e20] */
const val AC_DIRECT_DEBITING_FAILURE_HANDLING                   = 428L                /* [RFC 4006] */
const val AC_DIRECTION                                          = 514L                /* [RFC 5777] */
const val AC_DISCONNECT_CAUSE                                   = 273L                /* [RFC 6733] */
const val AC_DL_BUFFERING_SUGGESTED_PACKET_COUNT                = 1674L               /* [29.272-f10] */
const val AC_DOMAIN_NAME                                        = 1200L               /* [32-299-f10] */
const val AC_DOWNLINK_RATE_LIMIT                                = 4312L               /* [29.128-f10] */
const val AC_DRA_BINDING                                        = 2208L               /* [29.215-f00] */
const val AC_DRA_DEPLOYMENT                                     = 2206L               /* [29.215-f00] */
const val AC_DRM_CONTENT                                        = 1221L               /* [32-299-f10] */
const val AC_DRMP                                               = 301L                /* [RFC 7944] */
const val AC_DSA_FLAGS                                          = 1422L               /* [29.272-f10] */
const val AC_DSAI_TAG                                           = 711L                /* [29.329-f00] */
const val AC_DSR_FLAGS                                          = 1421L               /* [29.272-f10] */
const val AC_DYNAMIC_ADDRESS_FLAG                               = 2051L               /* [32-299-f10] */
const val AC_DYNAMIC_ADDRESS_FLAG_EXTENSION                     = 2068L               /* [32-299-f10] */
const val AC_E_UTRAN_CELL_GLOBAL_IDENTITY                       = 1602L               /* [29.272-f10] */
const val AC_E_UTRAN_VECTOR                                     = 1414L               /* [29.272-f10] */
const val AC_E2E_SEQUENCE                                       = 300L                /* [RFC 6733] */
const val AC_EAP_KEY_NAME                                       = 102L                /* [no_reference] */
const val AC_EAP_MASTER_SESSION_KEY                             = 464L                /* [no_reference] */
const val AC_EAP_PAYLOAD                                        = 462L                /* [no_reference] */
const val AC_EAP_REISSUED_PAYLOAD                               = 463L                /* [no_reference] */
const val AC_EARLY_MEDIA_DESCRIPTION                            = 1272L               /* [32-299-f10] */
const val AC_ECGI                                               = 2517L               /* [29.172-d10] */
const val AC_EDRX_CYCLE_LENGTH                                  = 1691L               /* [29.272-f10] */
const val AC_EDRX_CYCLE_LENGTH_VALUE                            = 1692L               /* [29.272-f10] */
const val AC_EMERGENCY_INFO                                     = 1687L               /* [29.272-f10] */
const val AC_EMERGENCY_SERVICES                                 = 1538L               /* [29.273-f10] */
const val AC_ENHANCED_COVERAGE_RESTRICTION                      = 3155L               /* [29.336-f10] */
const val AC_ENHANCED_COVERAGE_RESTRICTION_DATA                 = 3156L               /* [29.336-f10] */
const val AC_ENODEB_ID                                          = 4008L               /* [29.217-e20] */
const val AC_ENODEB_ID_S6                                       = 4008L               /* [29.272-f10] */
const val AC_ENVELOPE                                           = 1266L               /* [32-299-f10] */
const val AC_ENVELOPE_END_TIME                                  = 1267L               /* [32-299-f10] */
const val AC_ENVELOPE_REPORTING                                 = 1268L               /* [32-299-f10] */
const val AC_ENVELOPE_START_TIME                                = 1269L               /* [32-299-f10] */
const val AC_EPDG_ADDRESS                                       = 3425L               /* [32-299-f10] */
const val AC_EPS_LOCATION_INFORMATION                           = 1496L               /* [29.272-f10] */
const val AC_EPS_SUBSCRIBED_QOS_PROFILE                         = 1431L               /* [29.272-f10] */
const val AC_EPS_USER_STATE                                     = 1495L               /* [29.272-f10] */
const val AC_EQUIPMENT_STATUS                                   = 1445L               /* [29.272-f10] */
const val AC_EQUIVALENT_PLMN_LIST                               = 1637L               /* [29.272-f10] */
const val AC_ERP_AUTHORIZATION                                  = 1541L               /* [29.273-f10] */
const val AC_ERP_REALM                                          = 619L                /* [RFC 6942] */
const val AC_ERP_RK_REQUEST                                     = 618L                /* [RFC 6942] */
const val AC_ERROR_DIAGNOSTIC                                   = 1614L               /* [29.272-f10] */
const val AC_ERROR_MESSAGE                                      = 281L                /* [RFC 6733] */
const val AC_ERROR_REPORTING_HOST                               = 294L                /* [RFC 6733] */
const val AC_ESMLC_CELL_INFO                                    = 2552L               /* [29.172-d10] */
const val AC_ETH_ETHER_TYPE                                     = 550L                /* [RFC 5777] */
const val AC_ETH_OPTION                                         = 548L                /* [RFC 5777] */
const val AC_ETH_PROTO_TYPE                                     = 549L                /* [RFC 5777] */
const val AC_ETH_SAP                                            = 551L                /* [RFC 5777] */
const val AC_EUI64_ADDRESS                                      = 527L                /* [RFC 5777] */
const val AC_EUI64_ADDRESS_MASK                                 = 528L                /* [RFC 5777] */
const val AC_EUI64_ADDRESS_MASK_PATTERN                         = 529L                /* [RFC 5777] */
const val AC_EUTRAN_POSITIONING_DATA                            = 2516L               /* [29.172-d10] */
const val AC_EVENT                                              = 825L                /* [32-299-f10] */
const val AC_EVENT_CHARGING_TIMESTAMP                           = 1258L               /* [32-299-f10] */
const val AC_EVENT_HANDLING                                     = 3149L               /* [29.336-f10] */
const val AC_EVENT_REPORT_INDICATION                            = 1033L               /* [29.212-f10] */
const val AC_EVENT_THRESHOLD_EVENT_1F                           = 1661L               /* [29.272-f10] */
const val AC_EVENT_THRESHOLD_EVENT_1I                           = 1662L               /* [29.272-f10] */
const val AC_EVENT_THRESHOLD_RSRP                               = 1629L               /* [29.272-a60] */
const val AC_EVENT_THRESHOLD_RSRQ                               = 1630L               /* [29.272-a60] */
const val AC_EVENT_TIMESTAMP                                    = 55L                 /* [RFC 6733] */
const val AC_EVENT_TRIGGER                                      = 1006L               /* [29.212-f10] */
const val AC_EVENT_TRIGGER_R11                                  = 1006L               /* [29.212-f10] */
const val AC_EVENT_TYPE                                         = 823L                /* [32-299-f10] */
const val AC_EXCESS_TREATMENT                                   = 577L                /* [RFC 5777] */
const val AC_EXECUTION_TIME                                     = 2839L               /* [29.212-f10] */
const val AC_EXPERIMENTAL_RESULT                                = 297L                /* [RFC 6733] */
const val AC_EXPERIMENTAL_RESULT_CODE                           = 298L                /* [RFC 6733] */
const val AC_EXPIRATION_DATE                                    = 1439L               /* [29.272-f10] */
const val AC_EXPIRES                                            = 888L                /* [32-299-f10] */
const val AC_EXPIRY_TIME                                        = 709L                /* [29.329-f00] */
const val AC_EXPONENT                                           = 429L                /* [RFC 4006] */
const val AC_EXT_PDP_ADDRESS                                    = 1621L               /* [29.272-f10] */
const val AC_EXT_PDP_TYPE                                       = 1620L               /* [29.272-f10] */
const val AC_EXTENDED_APN_AMBR_DL                               = 2848L               /* [29.212-f10] */
const val AC_EXTENDED_APN_AMBR_UL                               = 2849L               /* [29.212-f10] */
const val AC_EXTENDED_ENODEB_ID                                 = 4013L               /* [29.217-e20] */
const val AC_EXTENDED_GBR_DL                                    = 2850L               /* [29.212-f10] */
const val AC_EXTENDED_GBR_UL                                    = 2851L               /* [29.212-f10] */
const val AC_EXTENDED_MAX_REQUESTED_BW_DL                       = 554L                /* [29.214-f10] */
const val AC_EXTENDED_MAX_REQUESTED_BW_UL                       = 555L                /* [29.214-f10] */
const val AC_EXTENDED_MAX_SUPPORTED_BW_DL                       = 556L                /* [29.214-f10] */
const val AC_EXTENDED_MAX_SUPPORTED_BW_UL                       = 557L                /* [29.214-f10] */
const val AC_EXTENDED_MIN_DESIRED_BW_DL                         = 558L                /* [29.214-f10] */
const val AC_EXTENDED_MIN_DESIRED_BW_UL                         = 559L                /* [29.214-f10] */
const val AC_EXTENDED_MIN_REQUESTED_BW_DL                       = 560L                /* [29.214-f10] */
const val AC_EXTENDED_MIN_REQUESTED_BW_UL                       = 561L                /* [29.214-f10] */
const val AC_EXTENDED_PCO                                       = 4313L               /* [29.128-f10] */
const val AC_EXTERNAL_CLIENT                                    = 1479L               /* [29.272-f10] */
const val AC_EXTERNAL_IDENTIFIER                                = 3111L               /* [29.336-f10] */
const val AC_EXTERNAL_IDENTIFIER_S6                             = 3111L               /* [29.272-f10] */
const val AC_FAILED_AVP                                         = 279L                /* [RFC 6733] */
const val AC_FEATURE_LIST                                       = 630L                /* [29.229-e20] */
const val AC_FEATURE_LIST_ID                                    = 629L                /* [29.229-e20] */
const val AC_FEATURE_LIST_S6T                                   = 630L                /* [29.336-f10] */
const val AC_FEATURE_LIST_T6                                    = 630L                /* [29.128-f10] */
const val AC_FEATURE_SUPPORTED_IN_FINAL_TARGET                  = 3012L               /* [29.368-e20] */
const val AC_FILE_REPAIR_SUPPORTED                              = 1224L               /* [32-299-f10] */
const val AC_FILTER_ID                                          = 11L                 /* [RFC 7155] */
const val AC_FILTER_Id                                          = 11L                 /* [RFC 2865] */
const val AC_FILTER_RULE                                        = 509L                /* [RFC 5777] */
const val AC_FILTER_RULE_PRECEDENCE                             = 510L                /* [RFC 5777] */
const val AC_FINAL_UNIT_ACTION                                  = 449L                /* [RFC 4006] */
const val AC_FINAL_UNIT_INDICATION                              = 430L                /* [RFC 4006] */
const val AC_FIRMWARE_REVISION                                  = 267L                /* [RFC 6733] */
const val AC_FIXED_USER_LOCATION_INFO                           = 2825L               /* [29.212-f10] */
const val AC_FLOW_DESCRIPTION                                   = 507L                /* [29.214-f10] */
const val AC_FLOW_DIRECTION                                     = 1080L               /* [29.212-f10] */
const val AC_FLOW_INFORMATION                                   = 1058L               /* [29.212-f10] */
const val AC_FLOW_LABEL                                         = 1057L               /* [29.212-f10] */
const val AC_FLOW_NUMBER                                        = 509L                /* [29.214-f10] */
const val AC_FLOW_STATUS                                        = 511L                /* [29.214-f10] */
const val AC_FLOW_USAGE                                         = 512L                /* [29.214-f10] */
const val AC_FLOWS                                              = 510L                /* [29.214-f10] */
const val AC_FRAGMENTATION_FLAG                                 = 536L                /* [RFC 5777] */
const val AC_FRAMED_APPLETALK_LINK                              = 37L                 /* [RFC 7155] */
const val AC_FRAMED_APPLETALK_NETWORK                           = 38L                 /* [RFC 7155] */
const val AC_FRAMED_APPLETALK_ZONE                              = 39L                 /* [RFC 7155] */
const val AC_FRAMED_COMPRESSION                                 = 13L                 /* [RFC 7155] */
const val AC_FRAMED_INTERFACE_ID                                = 96L                 /* [RFC 7155] */
const val AC_FRAMED_IP_ADDRESS                                  = 8L                  /* [RFC 7155] */
const val AC_FRAMED_IP_NETMASK                                  = 9L                  /* [RFC 7155] */
const val AC_FRAMED_IPV6_POOL                                   = 100L                /* [RFC 7155] */
const val AC_FRAMED_IPV6_PREFIX                                 = 97L                 /* [RFC 7155] */
const val AC_FRAMED_IPV6_ROUTE                                  = 99L                 /* [RFC 7155] */
const val AC_FRAMED_IPX_NETWORK                                 = 23L                 /* [RFC 7155] */
const val AC_FRAMED_MTU                                         = 12L                 /* [RFC 7155] */
const val AC_FRAMED_POOL                                        = 88L                 /* [RFC 7155] */
const val AC_FRAMED_PROTOCOL                                    = 7L                  /* [RFC 7155] */
const val AC_FRAMED_ROUTE                                       = 22L                 /* [RFC 7155] */
const val AC_FRAMED_ROUTING                                     = 10L                 /* [RFC 7155] */
const val AC_FROM_SIP_HEADER                                    = 644L                /* [29.229-e20] */
const val AC_FROM_SPEC                                          = 515L                /* [RFC 5777] */
const val AC_FULL_NETWORK_NAME                                  = 1516L               /* [29.273-f10] */
const val AC_G_S_U_POOL_IDENTIFIER                              = 453L                /* [RFC 4006] */
const val AC_G_S_U_POOL_REFERENCE                               = 457L                /* [RFC 4006] */
const val AC_GCS_IDENTIFIER                                     = 538L                /* [29.214-f10] */
const val AC_GEODETIC_INFORMATION                               = 1609L               /* [29.272-f10] */
const val AC_GEOGRAPHICAL_INFORMATION                           = 1608L               /* [29.272-f10] */
const val AC_GERAN_GANSS_POSTIONING_DATA                        = 2526L               /* [29.172-d10] */
const val AC_GERAN_POSITIONING_DATA                             = 2525L               /* [29.172-d10] */
const val AC_GERAN_POSITIONING_INFO                             = 2524L               /* [29.172-d10] */
const val AC_GERAN_VECTOR                                       = 1416L               /* [29.272-f10] */
const val AC_GGSN_ADDRESS                                       = 847L                /* [32-299-f10] */
const val AC_GMLC_ADDRESS                                       = 2405L               /* [29.173-e00] */
const val AC_GMLC_ADDRESS_S6                                    = 2405L               /* [29.173] */
const val AC_GMLC_NUMBER                                        = 1474L               /* [29.272-f10] */
const val AC_GMLC_RESTRICTION                                   = 1481L               /* [29.272-f10] */
const val AC_GMM_CAUSE                                          = 4304L               /* [29.128-f10] */
const val AC_GPRS_SUBSCRIPTION_DATA                             = 1467L               /* [29.272-f10] */
const val AC_GRANTED_SERVICE_UNIT                               = 431L                /* [RFC 4006] */
const val AC_GRANTED_VALIDITY_TIME                              = 3160L               /* [29.336-f10] */
const val AC_GROUP_MONITORING_EVENT_REPORT                      = 3165L               /* [29.336-f10] */
const val AC_GROUP_MONITORING_EVENT_REPORT_ITEM                 = 3166L               /* [29.336-f10] */
const val AC_GROUP_PLMN_ID                                      = 1677L               /* [29.272-f10] */
const val AC_GROUP_REPORTING_GUARD_TIMER                        = 3163L               /* [29.336-f10] */
const val AC_GROUP_SERVICE_ID                                   = 1676L               /* [29.272-f10] */
const val AC_GUARANTEED_BITRATE_DL                              = 1025L               /* [29.212-f10] */
const val AC_GUARANTEED_BITRATE_UL                              = 1026L               /* [29.212-f10] */
const val AC_HENB_BBF_FQDN                                      = 2803L               /* [29.212-f10] */
const val AC_HENB_LOCAL_IP_ADDRESS                              = 2804L               /* [29.212-f10] */
const val AC_HESSID                                             = 1525L               /* [29.273-f10] */
const val AC_HIGH_USER_PRIORITY                                 = 559L                /* [RFC 5777] */
const val AC_HOMOGENEOUS_SUPPORT_OF_IMS_VOICE_OVER_PS_SESSIONS  = 1493L               /* [29.272-f10] */
const val AC_HORIZONTAL_ACCURACY                                = 2505L               /* [29.172-d10] */
const val AC_HOST_IP_ADDRESS                                    = 257L                /* [RFC 6733] */
const val AC_HPLMN_ODB                                          = 1418L               /* [29.272-f10] */
const val AC_HSS_CAUSE                                          = 3109L               /* [29.336-f10] */
const val AC_HSS_ID                                             = 3325L               /* [29.338-f00] */
const val AC_ICMP_CODE                                          = 547L                /* [RFC 5777] */
const val AC_ICMP_TYPE                                          = 545L                /* [RFC 5777] */
const val AC_ICMP_TYPE_NUMBER                                   = 546L                /* [RFC 5777] */
const val AC_ICS_INDICATOR                                      = 1491L               /* [29.272-f10] */
const val AC_IDA_FLAGS                                          = 1441L               /* [29.272-f10] */
const val AC_IDENTITY_SET                                       = 708L                /* [29.329-f00] */
const val AC_IDENTITY_WITH_EMERGENCY_REGISTRATION               = 651L                /* [29.229-e20] */
const val AC_IDLE_STATUS_INDICATION                             = 4322L               /* [29.128-f00] */
const val AC_IDLE_STATUS_INDICATION_S6T                         = 4322L               /* [29.336-f10] */
const val AC_IDLE_STATUS_TIMESTAMP                              = 4323L               /* [29.128-f00] */
const val AC_IDLE_TIMEOUT                                       = 28L                 /* [RFC 7155] */
const val AC_IDR_FLAGS                                          = 1490L               /* [29.272-f10] */
const val AC_IM_INFORMATION                                     = 2110L               /* [OMADDSChargingData] */
const val AC_IMEI                                               = 1402L               /* [29.272-f10] */
const val AC_IMEI_CHANGE                                        = 3141L               /* [29.336-f10] */
const val AC_IMMEDIATE_RESPONSE_PREFERRED                       = 1412L               /* [29.272-f10] */
const val AC_IMS_APPLICATION_REFERENCE_IDENTIFIER               = 2601L               /* [32-299-f10] */
const val AC_IMS_CHARGING_IDENTIFIER                            = 841L                /* [32-299-f10] */
const val AC_IMS_COMMUNICATION_SERVICE_IDENTIFIER               = 1281L               /* [32-299-f10] */
const val AC_IMS_INFORMATION                                    = 876L                /* [32-299-f10] */
const val AC_IMS_VOICE_OVER_PS_SESSIONS_SUPPORTED               = 1492L               /* [29.272-f10] */
const val AC_IMSI_GROUP_ID                                      = 1675L               /* [29.272-f10] */
const val AC_IMSI_LIST                                          = 4009L               /* [29.217-e20] */
const val AC_IMSI_UNAUTHENTICATED_FLAG                          = 2308L               /* [32-299-f10] */
const val AC_INBAND_SECURITY_ID                                 = 299L                /* [RFC 6733] */
const val AC_INCOMING_TRUNK_GROUP_ID                            = 852L                /* [32-299-f10] */
const val AC_INCREMENTAL_COST                                   = 2062L               /* [32-299-f10] */
const val AC_INITIAL_CSEQ_SEQUENCE_NUMBER                       = 654L                /* [29.229-e20] */
const val AC_INITIAL_IMS_CHARGING_IDENTIFIER                    = 2321L               /* [32-299-f10] */
const val AC_INTEGRITY_KEY                                      = 626L                /* [29.229-e20] */
const val AC_INTER_OPERATOR_IDENTIFIER                          = 838L                /* [32-299-f10] */
const val AC_INTERFACE_ID                                       = 2003L               /* [32-299-f10] */
const val AC_INTERFACE_PORT                                     = 2004L               /* [32-299-f10] */
const val AC_INTERFACE_TEXT                                     = 2005L               /* [32-299-f10] */
const val AC_INTERFACE_TYPE                                     = 2006L               /* [32-299-f10] */
const val AC_INTERVAL_TIME                                      = 2539L               /* [29.172-d10] */
const val AC_IP_ADDRESS                                         = 518L                /* [RFC 5777] */
const val AC_IP_ADDRESS_END                                     = 521L                /* [RFC 5777] */
const val AC_IP_ADDRESS_MASK                                    = 522L                /* [RFC 5777] */
const val AC_IP_ADDRESS_RANGE                                   = 519L                /* [RFC 5777] */
const val AC_IP_ADDRESS_START                                   = 520L                /* [RFC 5777] */
const val AC_IP_BIT_MASK_WIDTH                                  = 523L                /* [RFC 5777] */
const val AC_IP_CAN_SESSION_CHARGING_SCOPE                      = 2827L               /* [29.212-f10] */
const val AC_IP_CAN_TYPE                                        = 1027L               /* [29.212-f10] */
const val AC_IP_DOMAIN_ID                                       = 537L                /* [29.214-f10] */
const val AC_IP_OPTION                                          = 537L                /* [RFC 5777] */
const val AC_IP_OPTION_TYPE                                     = 538L                /* [RFC 5777] */
const val AC_IP_OPTION_VALUE                                    = 539L                /* [RFC 5777] */
const val AC_IP_REALM_DEFAULT_INDICATION                        = 2603L               /* [32-299-f10] */
const val AC_IP_SM_GW_NAME                                      = 3101L               /* [29.336-f10] */
const val AC_IP_SM_GW_NUMBER                                    = 3100L               /* [29.336-f10] */
const val AC_IP_SM_GW_REALM                                     = 3112L               /* [29.336-f10] */
const val AC_IP_SM_GW_SM_DELIVERY_OUTCOME                       = 3320L               /* [29.338-f00] */
const val AC_ITEM_NUMBER                                        = 1419L               /* [29.272-f10] */
const val AC_JOB_TYPE                                           = 1623L               /* [29.272-f10] */
const val AC_KASME                                              = 1450L               /* [29.272-f10] */
const val AC_KC                                                 = 1453L               /* [29.272-f10] */
const val AC_KEY                                                = 581L                /* [RFC 6734] */
const val AC_KEY_LIFETIME                                       = 584L                /* [RFC 6734] */
const val AC_KEY_NAME                                           = 586L                /* [RFC 6734] */
const val AC_KEY_SPI                                            = 585L                /* [RFC 6734] */
const val AC_KEY_TYPE                                           = 582L                /* [RFC 6734] */
const val AC_KEYING_MATERIAL                                    = 583L                /* [RFC 6734] */
const val AC_LAST_UE_ACTIVITY_TIME                              = 1494L               /* [29.272-f10] */
const val AC_LCS_APN                                            = 1231L               /* [32-299-f10] */
const val AC_LCS_CAPABILITIES_SETS                              = 2404L               /* [29.173-e00] */
const val AC_LCS_CLIENT_DIALED_BY_MS                            = 1233L               /* [32-299-f10] */
const val AC_LCS_CLIENT_EXTERNAL_ID                             = 1234L               /* [32-299-f10] */
const val AC_LCS_CLIENT_ID                                      = 1232L               /* [32-299-f10] */
const val AC_LCS_CLIENT_NAME                                    = 1235L               /* [32-299-f10] */
const val AC_LCS_CLIENT_TYPE                                    = 1241L               /* [32-299-f10] */
const val AC_LCS_CODEWORD                                       = 2511L               /* [29.172-d10] */
const val AC_LCS_DATA_CODING_SCHEME                             = 1236L               /* [32-299-f10] */
const val AC_LCS_EPS_CLIENT_NAME                                = 2501L               /* [29.172-d10] */
const val AC_LCS_FORMAT_INDICATOR                               = 1237L               /* [32-299-f10] */
const val AC_LCS_INFO                                           = 1473L               /* [29.272-f10] */
const val AC_LCS_INFORMATION                                    = 878L                /* [32-299-f10] */
const val AC_LCS_NAME_STRING                                    = 1238L               /* [32-299-f10] */
const val AC_LCS_PRIORITY                                       = 2503L               /* [29.172-d10] */
const val AC_LCS_PRIVACY_CHECK                                  = 2512L               /* [29.172-d10] */
const val AC_LCS_PRIVACY_CHECK_NON_SESSION                      = 2521L               /* [29.172-d10] */
const val AC_LCS_PRIVACY_CHECK_SESSION                          = 2522L               /* [29.172-d10] */
const val AC_LCS_PRIVACYEXCEPTION                               = 1475L               /* [29.272-f10] */
const val AC_LCS_QOS                                            = 2504L               /* [29.172-d10] */
const val AC_LCS_QOS_CLASS                                      = 2523L               /* [29.172-d10] */
const val AC_LCS_REFERENCE_NUMBER                               = 2531L               /* [29.172-d10] */
const val AC_LCS_REQUESTOR_ID                                   = 1239L               /* [32-299-f10] */
const val AC_LCS_REQUESTOR_ID_STRING                            = 1240L               /* [32-299-f10] */
const val AC_LCS_REQUESTOR_NAME                                 = 2502L               /* [29.172-d10] */
const val AC_LCS_SERVICE_TYPE_ID                                = 2520L               /* [29.172-d10] */
const val AC_LCS_SUPPORTED_GAD_SHAPES                           = 2510L               /* [29.172-d10] */
const val AC_LIA_FLAGS                                          = 653L                /* [29.229-e20] */
const val AC_LINE_IDENTIFIER                                    = 500L                /* [29.229-e20] */
const val AC_LIPA_PERMISSION                                    = 1618L               /* [29.272-f10] */
const val AC_LIST_OF_MEASUREMENTS                               = 1625L               /* [29.272-f10] */
const val AC_LMSI                                               = 2400L               /* [29.173-e00] */
const val AC_LOCAL_GROUP_ID                                     = 1678L               /* [29.272-f10] */
const val AC_LOCAL_GW_INSERTED_INDICATION                       = 2604L               /* [32-299-f10] */
const val AC_LOCAL_SEQUENCE_NUMBER                              = 2063L               /* [32-299-f10] */
const val AC_LOCAL_TIME_ZONE                                    = 1649L               /* [29.272-f10] */
const val AC_LOCAL_TIME_ZONE_INDICATION                         = 718L                /* [29.329-f00] */
const val AC_LOCATION_AREA_IDENTITY                             = 1606L               /* [29.272-f10] */
const val AC_LOCATION_ESTIMATE                                  = 1242L               /* [32-299-f10] */
const val AC_LOCATION_ESTIMATE_TYPE                             = 1243L               /* [32-299-f10] */
const val AC_LOCATION_EVENT                                     = 2518L               /* [29.172-d10] */
const val AC_LOCATION_INFORMATION                               = 127L                /* [RFC 5580] */
const val AC_LOCATION_INFORMATION_CONFIGURATION                 = 3135L               /* [29.336-f10] */
const val AC_LOCATION_INFORMATION_CONFIGURATION_S6              = 3135L               /* [29.272-f10] */
const val AC_LOCATION_TYPE                                      = 1244L               /* [32-299-f10] */
const val AC_LOGGING_DURATION                                   = 1632L               /* [29.272-a60] */
const val AC_LOGGING_INTERVAL                                   = 1631L               /* [29.272-a60] */
const val AC_LOGICAL_ACCESS_ID                                  = 302L                /* [ETSI ES 283 034] */
const val AC_LOGIN_IP_HOST                                      = 14L                 /* [RFC 7155] */
const val AC_LOGIN_IPV6_HOST                                    = 98L                 /* [RFC 7155] */
const val AC_LOGIN_LAT_GROUP                                    = 36L                 /* [RFC 7155] */
const val AC_LOGIN_LAT_NODE                                     = 35L                 /* [RFC 7155] */
const val AC_LOGIN_LAT_PORT                                     = 63L                 /* [RFC 7155] */
const val AC_LOGIN_LAT_SERVICE                                  = 34L                 /* [RFC 7155] */
const val AC_LOGIN_SERVICE                                      = 15L                 /* [RFC 7155] */
const val AC_LOGIN_TCP_PORT                                     = 16L                 /* [RFC 7155] */
const val AC_LOOSE_ROUTE_INDICATION                             = 638L                /* [29.229-e20] */
const val AC_LOSS_OF_CONNECTIVITY_REASON                        = 3162L               /* [29.336-f10] */
const val AC_LOW_BALANCE_INDICATION                             = 2020L               /* [32-299-f10] */
const val AC_LOW_PRIORITY_INDICATOR                             = 2602L               /* [32-299-f10] */
const val AC_LOW_USER_PRIORITY                                  = 558L                /* [RFC 5777] */
const val AC_LRA_FLAGS                                          = 2549L               /* [29.172-d10] */
const val AC_LRR_FLAGS                                          = 2530L               /* [29.172-d10] */
const val AC_MAC_ADDRESS                                        = 524L                /* [RFC 5777] */
const val AC_MAC_ADDRESS_MASK                                   = 525L                /* [RFC 5777] */
const val AC_MAC_ADDRESS_MASK_PATTERN                           = 526L                /* [RFC 5777] */
const val AC_MANDATORY_CAPABILITY                               = 604L                /* [29.229-e20] */
const val AC_MAX_REQUESTED_BANDWIDTH                            = 313L                /* [29.234-b20] */
const val AC_MAX_REQUESTED_BANDWIDTH_DL                         = 515L                /* [29.214-f10] */
const val AC_MAX_REQUESTED_BANDWIDTH_UL                         = 516L                /* [29.214-f10] */
const val AC_MAX_SUPPORTED_BANDWIDTH_DL                         = 1083L               /* [29.212-f10] */
const val AC_MAX_SUPPORTED_BANDWIDTH_UL                         = 1084L               /* [29.212-f10] */
const val AC_MAXIMUM_BANDWIDTH                                  = 1082L               /* [29.212-f10] */
const val AC_MAXIMUM_DETECTION_TIME                             = 3131L               /* [29.336-f10] */
const val AC_MAXIMUM_LATENCY                                    = 3133L               /* [29.336-f10] */
const val AC_MAXIMUM_NUMBER_ACCESSES                            = 319L                /* [29.234-b20] */
const val AC_MAXIMUM_NUMBER_OF_REPORTS                          = 3128L               /* [29.336-f10] */
const val AC_MAXIMUM_NUMBER_OF_REPORTS_S6                       = 3128L               /* [29.272-f10] */
const val AC_MAXIMUM_RESPONSE_TIME                              = 3134L               /* [29.336-f10] */
const val AC_MAXIMUM_RETRANSMISSION_TIME                        = 3330L               /* [29.338-f00] */
const val AC_MAXIMUM_UE_AVAILABILITY_TIME                       = 3329L               /* [29.338-f00] */
const val AC_MAXIMUM_WAIT_TIME                                  = 1537L               /* [29.273-f10] */
const val AC_MBMS_GW_ADDRESS                                    = 2307L               /* [32-299-f10] */
const val AC_MBMS_2G_3G_INDICATOR                               = 907L                /* [29.061-f10] */
const val AC_MBMS_INFORMATION                                   = 880L                /* [32-299-f10] */
const val AC_MBMS_SERVICE_AREA                                  = 903L                /* [29.061-f10] */
const val AC_MBMS_SERVICE_TYPE                                  = 906L                /* [29.061-f10] */
const val AC_MBMS_SESSION_IDENTITY                              = 908L                /* [29.061-f10] */
const val AC_MBMS_USER_SERVICE_TYPE                             = 1225L               /* [32-299-f10] */
const val AC_MBSFN_AREA                                         = 1694L               /* [29.272-f10] */
const val AC_MBSFN_AREA_ID                                      = 1695L               /* [29.272-f10] */
const val AC_MCPTT_IDENTIFIER                                   = 547L                /* [29.214-f10] */
const val AC_MDT_ALLOWED_PLMN_ID                                = 1671L               /* [29.272-f10] */
const val AC_MDT_CONFIGURATION                                  = 1622L               /* [29.272-f10] */
const val AC_MDT_USER_CONSENT                                   = 1634L               /* [29.272-a60] */
const val AC_MEASUREMENT_PERIOD_LTE                             = 1655L               /* [29.272-f10] */
const val AC_MEASUREMENT_PERIOD_UMTS                            = 1656L               /* [29.272-f10] */
const val AC_MEASUREMENT_QUANTITY                               = 1660L               /* [29.272-f10] */
const val AC_MEDIA_COMPONENT_DESCRIPTION                        = 517L                /* [29.214-f10] */
const val AC_MEDIA_COMPONENT_NUMBER                             = 518L                /* [29.214-f10] */
const val AC_MEDIA_COMPONENT_STATUS                             = 549L                /* [29.214-f10] */
const val AC_MEDIA_INITIATOR_FLAG                               = 882L                /* [32-299-f10] */
const val AC_MEDIA_INITIATOR_PARTY                              = 1288L               /* [32-299-f10] */
const val AC_MEDIA_SUB_COMPONENT                                = 519L                /* [29.214-f10] */
const val AC_MEDIA_TYPE                                         = 520L                /* [29.214-f10] */
const val AC_MESSAGE_BODY                                       = 889L                /* [32-299-f10] */
const val AC_MESSAGE_CLASS                                      = 1213L               /* [32-299-f10] */
const val AC_MESSAGE_ID                                         = 1210L               /* [32-299-f10] */
const val AC_MESSAGE_SIZE                                       = 1212L               /* [32-299-f10] */
const val AC_MESSAGE_TYPE                                       = 1211L               /* [32-299-f10] */
const val AC_METERING_METHOD                                    = 1007L               /* [29.212-f10] */
const val AC_MIN_REQUESTED_BANDWIDTH_DL                         = 534L                /* [29.214-f10] */
const val AC_MIN_REQUESTED_BANDWIDTH_UL                         = 535L                /* [29.214-f10] */
const val AC_MIP_CAREOF_ADDRESS                                 = 487L                /* [RFC 5778] */
const val AC_MIP_FA_RK                                          = 1506L               /* [29.273-f10] */
const val AC_MIP_FA_RK_SPI                                      = 1507L               /* [29.273-f10] */
const val AC_MIP_HOME_AGENT_ADDRESS                             = 334L                /* [RFC 4004] */
const val AC_MIP_HOME_AGENT_HOST                                = 348L                /* [RFC 4004] */
const val AC_MIP6_AGENT_INFO                                    = 486L                /* [RFC 5447] */
const val AC_MIP6_FEATURE_VECTOR                                = 124L                /* [RFC 5447] */
const val AC_MIP6_HOME_LINK_PREFIX                              = 125L                /* [RFC 5447] */
const val AC_MM_CONTENT_TYPE                                    = 1203L               /* [32-299-f10] */
const val AC_MMBOX_STORAGE_REQUESTED                            = 1248L               /* [32-299-f10] */
const val AC_MME_ABSENT_USER_DIAGNOSTIC_SM                      = 3313L               /* [29.338-f00] */
const val AC_MME_LOCATION_INFORMATION                           = 1600L               /* [29.272-f10] */
const val AC_MME_LOCATION_INFORMATION_S6T                       = 1600L               /* [29.336-f10] */
const val AC_MME_NAME                                           = 2402L               /* [29.173-e00] */
const val AC_MME_NUMBER_FOR_MT_SMS                              = 1645L               /* [29.272-f10] */
const val AC_MME_REALM                                          = 2408L               /* [29.173-e00] */
const val AC_MME_SM_DELIVERY_OUTCOME                            = 3317L               /* [29.338-f00] */
const val AC_MME_USER_STATE                                     = 1497L               /* [29.272-f10] */
const val AC_MMS_INFORMATION                                    = 877L                /* [32-299-f10] */
const val AC_MMTEL_INFORMATION                                  = 2030L               /* [32-299-f10] */
const val AC_MMTEL_SSERVICE_TYPE                                = 2031L               /* [32-299-f10] */
const val AC_MO_LR                                              = 1485L               /* [29.272-f10] */
const val AC_MOBILE_NODE_IDENTIFIER                             = 506L                /* [29.273-f10] */
const val AC_MONITORING_DURATION                                = 3130L               /* [29.336-f10] */
const val AC_MONITORING_DURATION_S6                             = 3130L               /* [29.272-f10] */
const val AC_MONITORING_EVENT_CONFIG_STATUS                     = 3142L               /* [29.336-f10] */
const val AC_MONITORING_EVENT_CONFIG_STATUS_S6                  = 3142L               /* [29.272-f10] */
const val AC_MONITORING_EVENT_CONFIGURATION                     = 3122L               /* [29.336-f10] */
const val AC_MONITORING_EVENT_CONFIGURATION_T6                  = 3122L               /* [29.128-f10] */
const val AC_MONITORING_EVENT_CONFIGURATION_S6                  = 3122L               /* [29.272-f10] */
const val AC_MONITORING_EVENT_REPORT                            = 3123L               /* [29.336-f10] */
const val AC_MONITORING_EVENT_REPORT_STATUS                     = 3171L               /* [29.336-f10] */
const val AC_MONITORING_EVENT_REPORT_T6                         = 3123L               /* [29.128-f10] */
const val AC_MONITORING_EVENT_REPORT_S6                         = 3123L               /* [29.272-f10] */
const val AC_MONITORING_FLAGS                                   = 2828L               /* [29.212-f10] */
const val AC_MONITORING_KEY                                     = 1066L               /* [29.212-f10] */
const val AC_MONITORING_TIME                                    = 2810L               /* [29.212-f10] */
const val AC_MONITORING_TYPE                                    = 3127L               /* [29.336-f10] */
const val AC_MONITORING_TYPE_S6                                 = 3127L               /* [29.272-f10] */
const val AC_MONTE_LOCATION_TYPE                                = 3136L               /* [29.336-f10] */
const val AC_MONTH_OF_YEAR_MASK                                 = 565L                /* [RFC 5777] */
const val AC_MPS_IDENTIFIER                                     = 528L                /* [29.214-f10] */
const val AC_MPS_PRIORITY                                       = 1616L               /* [29.272-f10] */
const val AC_MSC_ABSENT_USER_DIAGNOSTIC_SM                      = 3314L               /* [29.338-f00] */
const val AC_MSC_NUMBER                                         = 2403L               /* [29.173-e00] */
const val AC_MSC_SM_DELIVERY_OUTCOME                            = 3318L               /* [29.338-f00] */
const val AC_MSISDN                                             = 701L                /* [29.329-f00] */
const val AC_MTC_ERROR_DIAGNOSTIC                               = 3203L               /* [29.337-e10] */
const val AC_MULTI_ROUND_TIME_OUT                               = 272L                /* [RFC 6733] */
const val AC_MULTIPLE_BBERF_ACTION                              = 2204L               /* [29.215-f00] */
const val AC_MULTIPLE_REGISTRATION_INDICATION                   = 648L                /* [29.229-e20] */
const val AC_MULTIPLE_SERVICES_CREDIT_CONTROL                   = 456L                /* [RFC 4006] */
const val AC_MULTIPLE_SERVICES_CREDIT_CONTROL_32299             = 456L                /* [32-299-f10] */
const val AC_MULTIPLE_SERVICES_INDICATOR                        = 455L                /* [RFC 4006] */
const val AC_MUTE_NOTIFICATION                                  = 2809L               /* [29.212-f10] */
const val AC_MWD_STATUS                                         = 3312L               /* [29.338-f00] */
const val AC_NAS_FILTER_RULE                                    = 400L                /* [RFC 7155] */
const val AC_NAS_IDENTIFIER                                     = 32L                 /* [RFC 7155] */
const val AC_NAS_IP_ADDRESS                                     = 4L                  /* [RFC 7155] */
const val AC_NAS_IPV6_ADDRESS                                   = 95L                 /* [RFC 7155] */
const val AC_NAS_PORT                                           = 5L                  /* [RFC 7155] */
const val AC_NAS_PORT_ID                                        = 87L                 /* [RFC 7155] */
const val AC_NAS_PORT_TYPE                                      = 61L                 /* [RFC 7155] */
const val AC_NBIFOM_MODE                                        = 2830L               /* [29.212-f10] */
const val AC_NBIFOM_SUPPORT                                     = 2831L               /* [29.212-f10] */
const val AC_NEGATED                                            = 517L                /* [RFC 5777] */
const val AC_NETLOC_ACCESS_SUPPORT                              = 2824L               /* [29.212-f10] */
const val AC_NETWORK_ACCESS_MODE                                = 1417L               /* [29.272-f10] */
const val AC_NETWORK_AREA_INFO_LIST                             = 4201L               /* [29.154-f00] */
const val AC_NETWORK_CONGESTION_AREA_REPORT                     = 4101L               /* [29.153-e00] */
const val AC_NETWORK_REQUEST_SUPPORT                            = 1024L               /* [29.212-f10] */
const val AC_NEXT_TARIFF                                        = 2057L               /* [32-299-f10] */
const val AC_NIDD_AUTHORIZATION_REQUEST                         = 3150L               /* [29.336-f10] */
const val AC_NIDD_AUTHORIZATION_RESPONSE                        = 3151L               /* [29.336-f10] */
const val AC_NIDD_AUTHORIZATION_UPDATE                          = 3161L               /* [29.336-f10] */
const val AC_NODE_FUNCTIONALITY                                 = 862L                /* [32-299-f10] */
const val AC_NODE_ID                                            = 2064L               /* [32-299-f10] */
const val AC_NODE_TYPE                                          = 3153L               /* [29.336-f10] */
const val AC_NON_3GPP_IP_ACCESS                                 = 1501L               /* [29.273-f10] */
const val AC_NON_3GPP_IP_ACCESS_APN                             = 1502L               /* [29.273-f10] */
const val AC_NON_3GPP_USER_DATA                                 = 1500L               /* [29.273-f10] */
const val AC_NON_IP_DATA                                        = 4315L               /* [29.128-f10] */
const val AC_NON_IP_DATA_DELIVERY_MECHANISM                     = 1682L               /* [29.272-f10] */
const val AC_NON_IP_PDN_TYPE_INDICATOR                          = 1681L               /* [29.272-f10] */
const val AC_NOR_FLAGS                                          = 1443L               /* [29.272-f10] */
const val AC_NOTIFICATION_TO_UE_USER                            = 1478L               /* [29.272-f10] */
const val AC_NS_REQUEST_TYPE                                    = 4102L               /* [29.153-e00] */
const val AC_NUMBER_OF_DIVERSIONS                               = 2034L               /* [32-299-f10] */
const val AC_NUMBER_OF_MESSAGES_SENT                            = 2019L               /* [32-299-f10] */
const val AC_NUMBER_OF_MESSAGES_SUCCESSFULLY_EXPLODED           = 2111L               /* [OMADDSChargingData] */
const val AC_NUMBER_OF_MESSAGES_SUCCESSFULLY_SENT               = 2112L               /* [OMADDSChargingData] */
const val AC_NUMBER_OF_PARTICIPANTS                             = 885L                /* [32-299-f10] */
const val AC_NUMBER_OF_RECEIVED_TALK_BURSTS                     = 1282L               /* [32-299-f10] */
const val AC_NUMBER_OF_REQUESTED_VECTORS                        = 1410L               /* [29.272-f10] */
const val AC_NUMBER_OF_TALK_BURSTS                              = 1283L               /* [32-299-f10] */
const val AC_NUMBER_OF_UE_PER_LOCATION_CONFIGURATION            = 4306L               /* [29.128-f10] */
const val AC_NUMBER_OF_UE_PER_LOCATION_REPORT                   = 4307L               /* [29.128-f10] */
const val AC_NUMBER_OF_UES                                      = 4209L               /* [29.154-f00] */
const val AC_NUMBER_PORTABILITY_ROUTING_INFORMATION             = 2024L               /* [32-299-f10] */
const val AC_OC_FEATURE_VECTOR                                  = 622L                /* [RFC 7683] */
const val AC_OC_OLR                                             = 623L                /* [RFC 7683] */
const val AC_OC_OLR_S6                                          = 623L                /* [29.272-f10] */
const val AC_OC_REDUCTION_PERCENTAGE                            = 627L                /* [RFC 7683] */
const val AC_OC_REPORT_TYPE                                     = 626L                /* [RFC 7683] */
const val AC_OC_SEQUENCE_NUMBER                                 = 624L                /* [RFC 7683] */
const val AC_OC_SUPPORTED_FEATURES                              = 621L                /* [RFC 7683] */
const val AC_OC_SUPPORTED_FEATURES_S6                           = 621L                /* [29.272-f10] */
const val AC_OC_VALIDITY_DURATION                               = 625L                /* [RFC 7683] */
const val AC_OCCURRENCE_INFO                                    = 2538L               /* [29.172-d10] */
const val AC_OFFLINE                                            = 1008L               /* [29.212-f10] */
const val AC_OFFLINE_CHARGING                                   = 1278L               /* [32-299-f10] */
const val AC_OFR_FLAGS                                          = 3328L               /* [29.338-f00] */
const val AC_OLD_REFERENCE_NUMBER                               = 3011L               /* [29.368-e20] */
const val AC_OMC_ID                                             = 1466L               /* [29.272-f10] */
const val AC_ONE_TIME_NOTIFICATION                              = 712L                /* [29.329-f00] */
const val AC_ONLINE                                             = 1009L               /* [29.212-f10] */
const val AC_ONLINE_CHARGING_FLAG                               = 2303L               /* [32-299-f10] */
const val AC_OPERATOR_DETERMINED_BARRING                        = 1425L               /* [29.272-f10] */
const val AC_OPERATOR_NAME                                      = 126L                /* [no_reference] */
const val AC_OPTIONAL_CAPABILITY                                = 605L                /* [29.229-e20] */
const val AC_ORIGIN_AAA_PROTOCOL                                = 408L                /* [RFC 7155] */
const val AC_ORIGIN_HOST                                        = 264L                /* [RFC 6733] */
const val AC_ORIGIN_REALM                                       = 296L                /* [RFC 6733] */
const val AC_ORIGIN_STATE_ID                                    = 278L                /* [RFC 6733] */
const val AC_ORIGINATING_IOI                                    = 839L                /* [32-299-f10] */
const val AC_ORIGINATING_LINE_INFO                              = 94L                 /* [RFC 7155] */
const val AC_ORIGINATING_REQUEST                                = 633L                /* [29.229-e20] */
const val AC_ORIGINATING_SIP_URI                                = 3326L               /* [29.338-f00] */
const val AC_ORIGINATION_TIME_STAMP                             = 1536L               /* [29.273-f10] */
const val AC_ORIGINATOR                                         = 864L                /* [32-299-f10] */
const val AC_ORIGINATOR_ADDRESS                                 = 886L                /* [32-299-f10] */
const val AC_ORIGINATOR_INTERFACE                               = 2009L               /* [32-299-f10] */
const val AC_ORIGINATOR_RECEIVED_ADDRESS                        = 2027L               /* [32-299-f10] */
const val AC_ORIGINATOR_SCCP_ADDRESS                            = 2008L               /* [32-299-f10] */
const val AC_OUTGOING_SESSION_ID                                = 2320L               /* [32-299-f10] */
const val AC_OUTGOING_TRUNK_GROUP_ID                            = 853L                /* [32-299-f10] */
const val AC_PACKET_FILTER_CONTENT                              = 1059L               /* [29.212-f10] */
const val AC_PACKET_FILTER_IDENTIFIER                           = 1060L               /* [29.212-f10] */
const val AC_PACKET_FILTER_INFORMATION                          = 1061L               /* [29.212-f10] */
const val AC_PACKET_FILTER_OPERATION                            = 1062L               /* [29.212-f10] */
const val AC_PACKET_FILTER_USAGE                                = 1072L               /* [29.212-f10] */
const val AC_PARTICIPANT_ACCESS_PRIORITY                        = 1259L               /* [32-299-f10] */
const val AC_PARTICIPANT_ACTION_TYPE                            = 2049L               /* [32-299-f10] */
const val AC_PARTICIPANT_GROUP                                  = 1260L               /* [32-299-f10] */
const val AC_PARTICIPANTS_INVOLVED                              = 887L                /* [32-299-f10] */
const val AC_PASSWORD_RETRY                                     = 75L                 /* [RFC 7155] */
const val AC_PATH                                               = 640L                /* [29.229-e20] */
const val AC_PAYLOAD                                            = 3004L               /* [29.368-e20] */
const val AC_PCC_RULE_STATUS                                    = 1019L               /* [29.212-f10] */
const val AC_PCRF_ADDRESS                                       = 2207L               /* [29.215-f00] */
const val AC_PCSCF_RESTORATION_INDICATION                       = 2826L               /* [29.212-f10] */
const val AC_PDG_ADDRESS                                        = 895L                /* [32-299-f10] */
const val AC_PDG_CHARGING_ID                                    = 896L                /* [32-299-f10] */
const val AC_PDN_CONNECTION_CHARGING_ID                         = 2050L               /* [32-299-f10] */
const val AC_PDN_CONNECTION_CONTINUITY                          = 1690L               /* [29.272-f10] */
const val AC_PDN_CONNECTION_ID                                  = 1065L               /* [29.212-f10] */
const val AC_PDN_GW_ALLOCATION_TYPE                             = 1438L               /* [29.272-f10] */
const val AC_PDN_TYPE                                           = 1456L               /* [29.272-f10] */
const val AC_PDP_ADDRESS                                        = 1227L               /* [32-299-f10] */
const val AC_PDP_ADDRESS_PREFIX_LENGTH                          = 2606L               /* [32-299-f10] */
const val AC_PDP_CONTEXT                                        = 1469L               /* [29.272-f10] */
const val AC_PDP_CONTEXT_TYPE                                   = 1247L               /* [32-299-f10] */
const val AC_PDP_TYPE                                           = 1470L               /* [29.272-f10] */
const val AC_PENDING_POLICY_COUNTER_CHANGE_TIME                 = 2906L               /* [29.219-f10] */
const val AC_PENDING_POLICY_COUNTER_INFORMATION                 = 2905L               /* [29.219-f10] */
const val AC_PERIODIC_COMMUNICATION_INDICATOR                   = 3115L               /* [29.336-f10] */
const val AC_PERIODIC_LDR_INFORMATION                           = 2540L               /* [29.172-d10] */
const val AC_PERIODIC_LOCATION_SUPPORT_INDICATOR                = 2550L               /* [29.172-d10] */
const val AC_PERIODIC_TIME                                      = 3117L               /* [29.336-f10] */
const val AC_PHYSICAL_ACCESS_ID                                 = 313L                /* [ETSI ES 283 034] */
const val AC_PLA_FLAGS                                          = 2546L               /* [29.172-d10] */
const val AC_PLMN_CLIENT                                        = 1482L               /* [29.272-f10] */
const val AC_PLMN_ID_LIST                                       = 2544L               /* [29.172-d10] */
const val AC_PLMN_ID_REQUESTED                                  = 3172L               /* [29.336-f10] */
const val AC_PLR_FLAGS                                          = 2545L               /* [29.172-d10] */
const val AC_POC_CHANGE_CONDITION                               = 1261L               /* [32-299-f10] */
const val AC_POC_CHANGE_TIME                                    = 1262L               /* [32-299-f10] */
const val AC_POC_CONTROLLING_ADDRESS                            = 858L                /* [32-299-f10] */
const val AC_POC_EVENT_TYPE                                     = 2025L               /* [32-299-f10] */
const val AC_POC_GROUP_NAME                                     = 859L                /* [32-299-f10] */
const val AC_POC_INFORMATION                                    = 879L                /* [32-299-f10] */
const val AC_POC_SERVER_ROLE                                    = 883L                /* [32-299-f10] */
const val AC_POC_SESSION_ID                                     = 1229L               /* [32-299-f10] */
const val AC_POC_SESSION_INITIATION_TYPE                        = 1277L               /* [32-299-f10] */
const val AC_POC_SESSION_TYPE                                   = 884L                /* [32-299-f10] */
const val AC_POC_USER_ROLE                                      = 1252L               /* [32-299-f10] */
const val AC_POC_USER_ROLE_IDS                                  = 1253L               /* [32-299-f10] */
const val AC_POC_USER_ROLE_INFO_UNITS                           = 1254L               /* [32-299-f10] */
const val AC_POLICY_COUNTER_IDENTIFIER                          = 2901L               /* [29.219-f10] */
const val AC_POLICY_COUNTER_STATUS                              = 2902L               /* [29.219-f10] */
const val AC_POLICY_COUNTER_STATUS_REPORT                       = 2903L               /* [29.219-f10] */
const val AC_PORT                                               = 530L                /* [RFC 5777] */
const val AC_PORT_END                                           = 533L                /* [RFC 5777] */
const val AC_PORT_LIMIT                                         = 62L                 /* [RFC 7155] */
const val AC_PORT_RANGE                                         = 531L                /* [RFC 5777] */
const val AC_PORT_START                                         = 532L                /* [RFC 5777] */
const val AC_POSITIONING_DATA                                   = 1245L               /* [32-299-f10] */
const val AC_POSITIONING_METHOD                                 = 1659L               /* [29.272-f10] */
const val AC_PPR_ADDRESS                                        = 2407L               /* [29.173-e00] */
const val AC_PPR_FLAGS                                          = 1508L               /* [29.273-f10] */
const val AC_PRA_INSTALL                                        = 2845L               /* [29.212-f10] */
const val AC_PRA_REMOVE                                         = 2846L               /* [29.212-f10] */
const val AC_PRE_EMPTION_CAPABILITY                             = 1047L               /* [29.212-f10] */
const val AC_PRE_EMPTION_CONTROL_INFO                           = 553L                /* [29.214-f10] */
const val AC_PRE_EMPTION_VULNERABILITY                          = 1048L               /* [29.212-f10] */
const val AC_PRE_PAGING_SUPPORTED                               = 717L                /* [29.329-f00] */
const val AC_PRECEDENCE                                         = 1010L               /* [29.212-f10] */
const val AC_PREFERRED_AOC_CURRENCY                             = 2315L               /* [32-299-f10] */
const val AC_PREFERRED_DATA_MODE                                = 1686L               /* [29.272-f10] */
const val AC_PRESENCE_REPORTING_AREA_ELEMENTS_LIST              = 2820L               /* [29.212-f10] */
const val AC_PRESENCE_REPORTING_AREA_IDENTIFIER                 = 2821L               /* [29.212-f10] */
const val AC_PRESENCE_REPORTING_AREA_INFORMATION                = 2822L               /* [29.212-f10] */
const val AC_PRESENCE_REPORTING_AREA_STATUS                     = 2823L               /* [29.212-f10] */
const val AC_PRIMARY_CHARGING_COLLECTION_FUNCTION_NAME          = 621L                /* [29.229-e20] */
const val AC_PRIMARY_EVENT_CHARGING_FUNCTION_NAME               = 619L                /* [29.229-e20] */
const val AC_PRIMARY_OCS_CHARGING_FUNCTION_NAME                 = 316L                /* [29.234-b20] */
const val AC_PRIORITIZED_LIST_INDICATOR                         = 2551L               /* [29.172-d10] */
const val AC_PRIORITY                                           = 1209L               /* [32-299-f10] */
const val AC_PRIORITY_INDICATION                                = 3006L               /* [29.368-e20] */
const val AC_PRIORITY_LEVEL                                     = 1046L               /* [29.212-f10] */
const val AC_PRIORITY_SHARING_INDICATOR                         = 550L                /* [29.214-f10] */
const val AC_PRIVILEDGED_SENDER_INDICATION                      = 652L                /* [29.229-e20] */
const val AC_PRODUCT_NAME                                       = 269L                /* [RFC 6733] */
const val AC_PROMPT                                             = 76L                 /* [RFC 7155] */
const val AC_PROSE_PERMISSION                                   = 3702L               /* [29.344] */
const val AC_PROSE_SUBSCRIPTION_DATA_S6                         = 3701L               /* [29.272-f10] */
const val AC_PROTOCOL                                           = 513L                /* [RFC 5777] */
const val AC_PROXY_HOST                                         = 280L                /* [RFC 6733] */
const val AC_PROXY_INFO                                         = 284L                /* [RFC 6733] */
const val AC_PROXY_STATE                                        = 33L                 /* [RFC 6733] */
const val AC_PS_APPEND_FREE_FORMAT_DATA                         = 867L                /* [32-299-f10] */
const val AC_PS_FREE_FORMAT_DATA                                = 866L                /* [32-299-f10] */
const val AC_PS_FURNISH_CHARGING_INFORMATION                    = 865L                /* [32-299-f10] */
const val AC_PS_INFORMATION                                     = 874L                /* [32-299-f10] */
const val AC_PS_TO_CS_SESSION_CONTINUITY                        = 1099L               /* [29.212-f10] */
const val AC_PSEUDONYM_INDICATOR                                = 2519L               /* [29.172-d10] */
const val AC_PUA_FLAGS                                          = 1442L               /* [29.272-f10] */
const val AC_PUBLIC_IDENTITY                                    = 601L                /* [29.229-e20] */
const val AC_PUR_FLAGS                                          = 1635L               /* [29.272-a60] */
const val AC_QOS_CAPABILITY                                     = 578L                /* [RFC 5777] */
const val AC_QOS_CLASS_IDENTIFIER                               = 1028L               /* [29.212-f10] */
const val AC_QOS_FILTER_RULE                                    = 407L                /* [RFC 7155] */
const val AC_QOS_INFORMATION                                    = 1016L               /* [29.212-f10] */
const val AC_QOS_NEGOTIATION                                    = 1029L               /* [29.212-f10] */
const val AC_QOS_PARAMETERS                                     = 576L                /* [RFC 5777] */
const val AC_QOS_PROFILE_ID                                     = 573L                /* [RFC 5777] */
const val AC_QOS_PROFILE_TEMPLATE                               = 574L                /* [RFC 5777] */
const val AC_QOS_RESOURCES                                      = 508L                /* [RFC 5777] */
const val AC_QOS_RULE_BASE_NAME                                 = 1074L               /* [29.212-f10] */
const val AC_QOS_RULE_DEFINITION                                = 1053L               /* [29.212-f10] */
const val AC_QOS_RULE_INSTALL                                   = 1051L               /* [29.212-f10] */
const val AC_QOS_RULE_NAME                                      = 1054L               /* [29.212-f10] */
const val AC_QOS_RULE_REMOVE                                    = 1052L               /* [29.212-f10] */
const val AC_QOS_RULE_REPORT                                    = 1055L               /* [29.212-f10] */
const val AC_QOS_SEMANTICS                                      = 575L                /* [RFC 5777] */
const val AC_QOS_SUBSCRIBED                                     = 1404L               /* [29.272-f10] */
const val AC_QOS_UPGRADE                                        = 1030L               /* [29.212-f10] */
const val AC_QUOTA_CONSUMPTION_TIME                             = 881L                /* [32-299-f10] */
const val AC_QUOTA_HOLDING_TIME                                 = 871L                /* [32-299-f10] */
const val AC_RAI                                                = 909L                /* [29.061-f10] */
const val AC_RAN_NAS_RELEASE_CAUSE                              = 2819L               /* [29.212-f10] */
const val AC_RAN_RULE_SUPPORT                                   = 2832L               /* [29.212-f10] */
const val AC_RANAP_CAUSE                                        = 4303L               /* [29.128-f10] */
const val AC_RAND                                               = 1447L               /* [29.272-f10] */
const val AC_RAR_FLAGS                                          = 1522L               /* [29.273-f10] */
const val AC_RAT_FREQUENCY_SELECTION_PRIORITY_ID                = 1440L               /* [29.272-f10] */
const val AC_RAT_TYPE                                           = 1032L               /* [29.212-f10] */
const val AC_RAT_TYPE_850                                       = 1032L               /* [29.212-850] */
const val AC_RAT_TYPE_S6                                        = 1032L               /* [29.272-f20] */
const val AC_RATE_CONTROL_MAX_MESSAGE_SIZE                      = 3937L               /* [32-299-f10] */
const val AC_RATE_CONTROL_MAX_RATE                              = 3938L               /* [32-299-f10] */
const val AC_RATE_CONTROL_TIME_UNIT                             = 3939L               /* [32-299-f10] */
const val AC_RATE_ELEMENT                                       = 2058L               /* [32-299-f10] */
const val AC_RATING_GROUP                                       = 432L                /* [RFC 4006] */
const val AC_RCAF_ID                                            = 4010L               /* [29.217-e20] */
const val AC_RDR_FLAGS                                          = 3323L               /* [29.338-f00] */
const val AC_RDS_INDICATOR                                      = 1697L               /* [29.272-f10] */
const val AC_RE_AUTH_REQUEST_TYPE                               = 285L                /* [RFC 6733] */
const val AC_RE_SYNCHRONIZATION_INFO                            = 1411L               /* [29.272-f10] */
const val AC_REACHABILITY_INFORMATION                           = 3140L               /* [29.336-f10] */
const val AC_REACHABILITY_INFORMATION_S6                        = 3140L               /* [29.272-f10] */
const val AC_REACHABILITY_TYPE                                  = 3132L               /* [29.336-f10] */
const val AC_REACHABILITY_TYPE_S6                               = 3132L               /* [29.272-f10] */
const val AC_READ_REPLY_REPORT_REQUESTED                        = 1222L               /* [32-299-f10] */
const val AC_REAL_TIME_TARIFF_INFORMATION                       = 2305L               /* [32-299-f10] */
const val AC_REASON_CODE                                        = 616L                /* [29.229-e20] */
const val AC_REASON_INFO                                        = 617L                /* [29.229-e20] */
const val AC_RECEIVED_TALK_BURST_TIME                           = 1284L               /* [32-299-f10] */
const val AC_RECEIVED_TALK_BURST_VOLUME                         = 1285L               /* [32-299-f10] */
const val AC_RECIPIENT_ADDRESS                                  = 1201L               /* [32-299-f10] */
const val AC_RECIPIENT_INFO                                     = 2026L               /* [32-299-f10] */
const val AC_RECIPIENT_RECEIVED_ADDRESS                         = 2028L               /* [32-299-f10] */
const val AC_RECIPIENT_SCCP_ADDRESS                             = 2010L               /* [32-299-f10] */
const val AC_RECORD_ROUTE                                       = 646L                /* [29.229-e20] */
const val AC_REDIRECT_ADDRESS_TYPE                              = 433L                /* [RFC 4006] */
const val AC_REDIRECT_HOST                                      = 292L                /* [RFC 6733] */
const val AC_REDIRECT_HOST_USAGE                                = 261L                /* [RFC 6733] */
const val AC_REDIRECT_INFORMATION                               = 1085L               /* [29.212-f10] */
const val AC_REDIRECT_MAX_CACHE_TIME                            = 262L                /* [RFC 6733] */
const val AC_REDIRECT_SERVER                                    = 434L                /* [RFC 4006] */
const val AC_REDIRECT_SERVER_ADDRESS                            = 435L                /* [RFC 4006] */
const val AC_REDIRECT_SUPPORT                                   = 1086L               /* [29.212-f10] */
const val AC_REFERENCE_ID                                       = 4202L               /* [29.154-f00] */
const val AC_REFERENCE_ID_VALIDITY_TIME                         = 3148L               /* [29.336-f10] */
const val AC_REFERENCE_ID_VALIDITY_TIME_S6                      = 3148L               /* [29.272-f10] */
const val AC_REFERENCE_NUMBER                                   = 3007L               /* [29.368-e20] */
const val AC_REFUND_INFORMATION                                 = 2022L               /* [32-299-f10] */
const val AC_REGIONAL_SUBSCRIPTION_ZONE_CODE                    = 1446L               /* [29.272-f10] */
const val AC_RELAY_NODE_INDICATOR                               = 1633L               /* [29.272-a60] */
const val AC_REMAINING_BALANCE                                  = 2021L               /* [32-299-f10] */
const val AC_REMOVAL_OF_ACCESS                                  = 2842L               /* [29.212-f10] */
const val AC_REPLY_APPLIC_ID                                    = 1223L               /* [32-299-f10] */
const val AC_REPLY_MESSAGE                                      = 18L                 /* [RFC 7155] */
const val AC_REPLY_PATH_REQUESTED                               = 2011L               /* [32-299-f10] */
const val AC_REPORT_AMOUNT                                      = 1628L               /* [29.272-a60] */
const val AC_REPORT_INTERVAL                                    = 1627L               /* [29.272-a60] */
const val AC_REPORTING_AMOUNT                                   = 2541L               /* [29.172-d10] */
const val AC_REPORTING_INTERVAL                                 = 2542L               /* [29.172-d10] */
const val AC_REPORTING_LEVEL                                    = 1011L               /* [29.212-f10] */
const val AC_REPORTING_PLMN_LIST                                = 2543L               /* [29.172-d10] */
const val AC_REPORTING_REASON                                   = 872L                /* [32.299-e50] */
const val AC_REPORTING_RESTRICTION                              = 4011L               /* [29.217-e20] */
const val AC_REPORTING_TRIGGER                                  = 1626L               /* [29.272-f10] */
const val AC_REPOSITORY_DATA_ID                                 = 715L                /* [29.329-f00] */
const val AC_REQUEST_STATUS                                     = 3008L               /* [29.368-e20] */
const val AC_REQUEST_TYPE                                       = 2838L               /* [29.212-f10] */
const val AC_REQUESTED_ACTION                                   = 436L                /* [RFC 4006] */
const val AC_REQUESTED_DOMAIN                                   = 706L                /* [29.329-f00] */
const val AC_REQUESTED_EUTRAN_AUTHENTICATION_INFO               = 1408L               /* [29.272-f10] */
const val AC_REQUESTED_NODES                                    = 713L                /* [29.329-f00] */
const val AC_REQUESTED_PARTY_ADDRESS                            = 1251L               /* [32-299-f10] */
const val AC_REQUESTED_RETRANSMISSION_TIME                      = 3331L               /* [29.338-f00] */
const val AC_REQUESTED_SERVICE_UNIT                             = 437L                /* [RFC 4006] */
const val AC_REQUESTED_UTRAN_GERAN_AUTHENTICATION_INFO          = 1409L               /* [29.272-f10] */
const val AC_REQUESTED_VALIDITY_TIME                            = 3159L               /* [29.336-f10] */
const val AC_REQUIRED_ACCESS_INFO                               = 536L                /* [29.214-f10] */
const val AC_REQUIRED_MBMS_BEARER_CAPABILITIES                  = 901L                /* [29.061-f10] */
const val AC_RESERVATION_PRIORITY                               = 458L                /* [ETSI-TS-183-017] */
const val AC_RESET_ID                                           = 1670L               /* [29.272-f10] */
const val AC_RESOURCE_ALLOCATION_NOTIFICATION                   = 1063L               /* [29.212-f10] */
const val AC_RESOURCE_RELEASE_NOTIFICATION                      = 2841L               /* [29.212-f10] */
const val AC_RESPONSE_TIME                                      = 2509L               /* [29.172-d10] */
const val AC_RESTORATION_INFO                                   = 649L                /* [29.229-e20] */
const val AC_RESTORATION_PRIORITY                               = 1663L               /* [29.272-f10] */
const val AC_RESTRICTED_PLMN_LIST                               = 3157L               /* [29.336-f10] */
const val AC_RESTRICTION_FILTER_RULE                            = 438L                /* [RFC 4006] */
const val AC_RESULT_CODE                                        = 268L                /* [RFC 6733] */
const val AC_RETRY_INTERVAL                                     = 541L                /* [29.214-f10] */
const val AC_REVALIDATION_TIME                                  = 1042L               /* [29.212-f10] */
const val AC_RIA_FLAGS                                          = 2411L               /* [29.173-e00] */
const val AC_RIR_FLAGS                                          = 3167L               /* [29.336-f10] */
const val AC_ROAMING_INFORMATION                                = 3139L               /* [29.336-f10] */
const val AC_ROAMING_RESTRICTED_DUE_TO_UNSUPPORTED_FEATURE      = 1457L               /* [29.272-f10] */
const val AC_ROLE_OF_NODE                                       = 829L                /* [32-299-f10] */
const val AC_ROUTE_RECORD                                       = 282L                /* [RFC 6733] */
const val AC_ROUTING_AREA_IDENTITY                              = 1605L               /* [29.272-f10] */
const val AC_ROUTING_FILTER                                     = 1078L               /* [29.212-f10] */
const val AC_ROUTING_IP_ADDRESS                                 = 1079L               /* [29.212-f10] */
const val AC_ROUTING_POLICY                                     = 312L                /* [29.234-b20] */
const val AC_ROUTING_RULE_DEFINITION                            = 1076L               /* [29.212-f10] */
const val AC_ROUTING_RULE_FAILURE_CODE                          = 2834L               /* [29.212-f10] */
const val AC_ROUTING_RULE_IDENTIFIER                            = 1077L               /* [29.212-f10] */
const val AC_ROUTING_RULE_INSTALL                               = 1081L               /* [29.212-f10] */
const val AC_ROUTING_RULE_REMOVE                                = 1075L               /* [29.212-f10] */
const val AC_ROUTING_RULE_REPORT                                = 2835L               /* [29.212-f10] */
const val AC_RR_BANDWIDTH                                       = 521L                /* [29.214-f10] */
const val AC_RRC_CAUSE_COUNTER                                  = 4318L               /* [29.128-f10] */
const val AC_RRC_COUNTER_TIMESTAMP                              = 4320L               /* [29.128-f10] */
const val AC_RS_BANDWIDTH                                       = 522L                /* [29.214-f10] */
const val AC_RUCI_ACTION                                        = 4012L               /* [29.217-e20] */
const val AC_RULE_ACTIVATION_TIME                               = 1043L               /* [29.212-f10] */
const val AC_RULE_DEACTIVATION_TIME                             = 1044L               /* [29.212-f10] */
const val AC_RULE_FAILURE_CODE                                  = 1031L               /* [29.212-f10] */
const val AC_RX_REQUEST_TYPE                                    = 533L                /* [29.214-f10] */
const val AC_S_VID_END                                          = 554L                /* [RFC 5777] */
const val AC_S_VID_START                                        = 553L                /* [RFC 5777] */
const val AC_S1AP_CAUSE                                         = 4302L               /* [29.128-f10] */
const val AC_S6T_HSS_CAUSE                                      = 3154L               /* [29.336-f10] */
const val AC_SAR_FLAGS                                          = 655L                /* [29.229-e20] */
const val AC_SC_ADDRESS                                         = 3300L               /* [29.338-f00] */
const val AC_SCALE_FACTOR                                       = 2059L               /* [32-299-f10] */
const val AC_SCEF_ID                                            = 3125L               /* [29.336-f10] */
const val AC_SCEF_ID_S6                                         = 3125L               /* [29.272-f10] */
const val AC_SCEF_REALM                                         = 1684L               /* [29.272-f10] */
const val AC_SCEF_REFERENCE_ID                                  = 3124L               /* [29.336-f10] */
const val AC_SCEF_REFERENCE_ID_FOR_DELETION                     = 3126L               /* [29.336-f10] */
const val AC_SCEF_REFERENCE_ID_FOR_DELETION_S6                  = 3126L               /* [29.272-f10] */
const val AC_SCEF_REFERENCE_ID_S6                               = 3124L               /* [29.272-f10] */
const val AC_SCEF_WAIT_TIME                                     = 4316L               /* [29.128-f10] */
const val AC_SCHEDULED_COMMUNICATION_TIME                       = 3118L               /* [29.336-f10] */
const val AC_SCS_ADDRESS                                        = 3941L               /* [32-299-f10] */
const val AC_SCS_AS_ADDRESS                                     = 3940L               /* [32-299-f10] */
const val AC_SCS_IDENTITY                                       = 3104L               /* [29.336-f10] */
const val AC_SCS_REALM                                          = 3942L               /* [32-299-f10] */
const val AC_SCSCF_RESTORATION_INFO                             = 639L                /* [29.229-e20] */
const val AC_SDP_ANSWER_TIMESTAMP                               = 1275L               /* [32-299-f10] */
const val AC_SDP_MEDIA_COMPONENT                                = 843L                /* [32-299-f10] */
const val AC_SDP_MEDIA_DESCRIPTION                              = 845L                /* [32-299-f10] */
const val AC_SDP_MEDIA_NAME                                     = 844L                /* [32-299-f10] */
const val AC_SDP_OFFER_TIMESTAMP                                = 1274L               /* [32-299-f10] */
const val AC_SDP_SESSION_DESCRIPTION                            = 842L                /* [32-299-f10] */
const val AC_SDP_TIMESTAMPS                                     = 1273L               /* [32-299-f10] */
const val AC_SDP_TYPE                                           = 2036L               /* [32-299-f10] */
const val AC_SECONDARY_CHARGING_COLLECTION_FUNCTION_NAME        = 622L                /* [29.229-e20] */
const val AC_SECONDARY_EVENT_CHARGING_FUNCTION_NAME             = 620L                /* [29.229-e20] */
const val AC_SECONDARY_OCS_CHARGING_FUNCTION_NAME               = 317L                /* [29.234-b20] */
const val AC_SECURITY_PARAMETER_INDEX                           = 1056L               /* [29.212-f10] */
const val AC_SEND_DATA_INDICATION                               = 710L                /* [29.329-f00] */
const val AC_SEQUENCE_NUMBER                                    = 716L                /* [29.329-f00] */
const val AC_SERVED_PARTY_IP_ADDRESS                            = 848L                /* [32-299-f10] */
const val AC_SERVER_ASSIGNMENT_TYPE                             = 614L                /* [29.229-e20] */
const val AC_SERVER_CAPABILITIES                                = 603L                /* [29.229-e20] */
const val AC_SERVER_NAME                                        = 602L                /* [29.229-e20] */
const val AC_SERVICE_AREA_IDENTITY                              = 1607L               /* [29.272-f10] */
const val AC_SERVICE_AUTHORIZATION_INFO                         = 548L                /* [29.214-f10] */
const val AC_SERVICE_CONTEXT_ID                                 = 461L                /* [RFC 4006] */
const val AC_SERVICE_DATA                                       = 3107L               /* [29.336-f10] */
const val AC_SERVICE_DATA_CONTAINER                             = 2040L               /* [32-299-f10] */
const val AC_SERVICE_GENERIC_INFORMATION                        = 1256L               /* [OMADDSChargingData] */
const val AC_SERVICE_ID                                         = 855L                /* [32-299-f10] */
const val AC_SERVICE_ID_S6M                                     = 3103L               /* [29.336-f10] */
const val AC_SERVICE_IDENTIFIER                                 = 439L                /* [RFC 4006] */
const val AC_SERVICE_INDICATION                                 = 704L                /* [29.329-f00] */
const val AC_SERVICE_INFO_STATUS                                = 527L                /* [29.214-f10] */
const val AC_SERVICE_INFORMATION                                = 873L                /* [32-299-f10] */
const val AC_SERVICE_MODE                                       = 2032L               /* [32-299-f10] */
const val AC_SERVICE_PARAMETER_INFO                             = 440L                /* [RFC 4006] */
const val AC_SERVICE_PARAMETER_TYPE                             = 441L                /* [RFC 4006] */
const val AC_SERVICE_PARAMETER_VALUE                            = 442L                /* [RFC 4006] */
const val AC_SERVICE_PARAMETERS                                 = 3105L               /* [29.336-f10] */
const val AC_SERVICE_REPORT                                     = 3152L               /* [29.336-f10] */
const val AC_SERVICE_RESULT                                     = 3146L               /* [29.336-f10] */
const val AC_SERVICE_RESULT_CODE                                = 3147L               /* [29.336-f10] */
const val AC_SERVICE_SELECTION                                  = 493L                /* [RFC 5778] */
const val AC_SERVICE_SPECIFIC_DATA                              = 863L                /* [32-299-f10] */
const val AC_SERVICE_SPECIFIC_INFO                              = 1249L               /* [32-299-f10] */
const val AC_SERVICE_SPECIFIC_TYPE                              = 1257L               /* [32-299-f10] */
const val AC_SERVICE_TYPE                                       = 6L                  /* [RFC 7155] */
const val AC_SERVICE_TYPE_32_299                                = 2031L               /* [32-299-f10] */
const val AC_SERVICE_TYPE_S6                                    = 1483L               /* [29.272-f10] */
const val AC_SERVICE_URN                                        = 525L                /* [29.214-f10] */
const val AC_SERVICETYPEIDENTITY                                = 1484L               /* [29.272-f10] */
const val AC_SERVING_NODE                                       = 2401L               /* [29.173-e00] */
const val AC_SERVING_NODE_INDICATION                            = 714L                /* [29.329-f00] */
const val AC_SERVING_NODE_T4                                    = 2401L               /* [29.336-f10] */
const val AC_SERVING_NODE_TYPE                                  = 2047L               /* [32-299-f10] */
const val AC_SERVING_PLMN_RATE_CONTROL                          = 4310L               /* [29.128-f10] */
const val AC_SESSION_BINDING                                    = 270L                /* [RFC 6733] */
const val AC_SESSION_ID                                         = 263L                /* [RFC 6733] */
const val AC_SESSION_LINKING_INDICATOR                          = 1064L               /* [29.212-f10] */
const val AC_SESSION_PRIORITY                                   = 650L                /* [29.229-e20] */
const val AC_SESSION_RELEASE_CAUSE                              = 1045L               /* [29.212-f10] */
const val AC_SESSION_REQUEST_TYPE                               = 311L                /* [29.234-b20] */
const val AC_SESSION_SERVER_FAILOVER                            = 271L                /* [RFC 6733] */
const val AC_SESSION_TIMEOUT                                    = 27L                 /* [RFC 6733] */
const val AC_SGI_PTP_TUNNELLING_METHOD                          = 3931L               /* [32-299-f10] */
const val AC_SGS_MME_IDENTITY                                   = 1664L               /* [29.272-f10] */
const val AC_SGSN_ABSENT_USER_DIAGNOSTIC_SM                     = 3315L               /* [29.338-f00] */
const val AC_SGSN_ADDRESS                                       = 1228L               /* [32-299-f10] */
const val AC_SGSN_LOCATION_INFORMATION                          = 1601L               /* [29.272-f10] */
const val AC_SGSN_LOCATION_INFORMATION_S6T                      = 1601L               /* [29.336-f10] */
const val AC_SGSN_NAME                                          = 2409L               /* [29.173-c30] */
const val AC_SGSN_NUMBER                                        = 1489L               /* [29.272-f10] */
const val AC_SGSN_REALM                                         = 2410L               /* [29.173-c30] */
const val AC_SGSN_SM_DELIVERY_OUTCOME                           = 3319L               /* [29.338-f00] */
const val AC_SGSN_USER_STATE                                    = 1498L               /* [29.272-f10] */
const val AC_SGW_ADDRESS                                        = 2067L               /* [32-299-f10] */
const val AC_SGW_CHANGE                                         = 2065L               /* [32-299-f10] */
const val AC_SHARING_KEY_DL                                     = 539L                /* [29.214-f10] */
const val AC_SHARING_KEY_UL                                     = 540L                /* [29.214-f10] */
const val AC_SHORT_NETWORK_NAME                                 = 1517L               /* [29.273-f10] */
const val AC_SIP_AUTH_DATA_ITEM                                 = 612L                /* [29.229-e20] */
const val AC_SIP_AUTH_DATA_ITEM_WX                              = 612L                /* [29.234-b20] */
const val AC_SIP_AUTHENTICATE                                   = 609L                /* [29.229-e20] */
const val AC_SIP_AUTHENTICATION_CONTEXT                         = 611L                /* [29.229-e20] */
const val AC_SIP_AUTHENTICATION_SCHEME                          = 608L                /* [29.229-e20] */
const val AC_SIP_AUTHORIZATION                                  = 610L                /* [29.229-e20] */
const val AC_SIP_DIGEST_AUTHENTICATE                            = 635L                /* [29.229-e20] */
const val AC_SIP_FORKING_INDICATION                             = 523L                /* [29.214-f10] */
const val AC_SIP_ITEM_NUMBER                                    = 613L                /* [29.229-e20] */
const val AC_SIP_METHOD                                         = 824L                /* [32-299-f10] */
const val AC_SIP_NUMBER_AUTH_ITEMS                              = 607L                /* [29.229-e20] */
const val AC_SIP_REQUEST_TIMESTAMP                              = 834L                /* [32-299-f10] */
const val AC_SIP_REQUEST_TIMESTAMP_FRACTION                     = 2301L               /* [32-299-f10] */
const val AC_SIP_RESPONSE_TIMESTAMP                             = 835L                /* [32-299-f10] */
const val AC_SIP_RESPONSE_TIMESTAMP_FRACTION                    = 2302L               /* [32-299-f10] */
const val AC_SIPTO_LOCAL_NETWORK_PERMISSION                     = 1665L               /* [29.272-f10] */
const val AC_SIPTO_PERMISSION                                   = 1613L               /* [29.272-f10] */
const val AC_SIR_FLAGS                                          = 3110L               /* [29.336-f10] */
const val AC_SL_REQUEST_TYPE                                    = 2904L               /* [29.219-f10] */
const val AC_SLG_LOCATION_TYPE                                  = 2500L               /* [29.172-d10] */
const val AC_SM_BACK_OFF_TIMER                                  = 1534L               /* [29.273-f10] */
const val AC_SM_CAUSE                                           = 4305L               /* [29.128-f10] */
const val AC_SM_DELIVERY_CAUSE                                  = 3321L               /* [29.338-f00] */
const val AC_SM_DELIVERY_FAILURE_CAUSE                          = 3303L               /* [29.338-f00] */
const val AC_SM_DELIVERY_NOT_INTENDED                           = 3311L               /* [29.338-f00] */
const val AC_SM_DELIVERY_OUTCOME                                = 3316L               /* [29.338-f00] */
const val AC_SM_DELIVERY_OUTCOME_T4                             = 3200L               /* [29.337-e10] */
const val AC_SM_DELIVERY_START_TIME                             = 3307L               /* [29.338-f00] */
const val AC_SM_DELIVERY_TIMER                                  = 3306L               /* [29.338-f00] */
const val AC_SM_DIAGNOSTIC_INFO                                 = 3305L               /* [29.338-f00] */
const val AC_SM_DISCHARGE_TIME                                  = 2012L               /* [32-299-f10] */
const val AC_SM_ENUMERATED_DELIVERY_FAILURE_CAUSE               = 3304L               /* [29.338-f00] */
const val AC_SM_MESSAGE_TYPE                                    = 2007L               /* [32-299-f10] */
const val AC_SM_PROTOCOL_ID                                     = 2013L               /* [32-299-f10] */
const val AC_SM_RP_MTI                                          = 3308L               /* [29.338-f00] */
const val AC_SM_RP_SMEA                                         = 3309L               /* [29.338-f00] */
const val AC_SM_RP_UI                                           = 3301L               /* [29.338-f00] */
const val AC_SM_SERVICE_TYPE                                    = 2029L               /* [32-299-f10] */
const val AC_SM_STATUS                                          = 2014L               /* [32-299-f10] */
const val AC_SM_USER_DATA_HEADER                                = 2015L               /* [32-299-f10] */
const val AC_SMS_APPLICATION_PORT_ID                            = 3010L               /* [29.368-e20] */
const val AC_SMS_GMSC_ADDRESS                                   = 3332L               /* [29.338-f00] */
const val AC_SMS_GMSC_ALERT_EVENT                               = 3333L               /* [29.338-f00] */
const val AC_SMS_INFORMATION                                    = 2000L               /* [32-299-f10] */
const val AC_SMS_NODE                                           = 2016L               /* [32-299-f10] */
const val AC_SMS_REGISTER_REQUEST                               = 1648L               /* [29.272-f10] */
const val AC_SMSC_ADDRESS                                       = 2017L               /* [32-299-f10] */
const val AC_SMSMI_CORRELATION_ID                               = 3324L               /* [29.338-f00] */
const val AC_SN_REQUEST_TYPE                                    = 2907L               /* [29.219-f10] */
const val AC_SOFTWARE_VERSION                                   = 1403L               /* [29.272-f10] */
const val AC_SPECIFIC_ACTION                                    = 513L                /* [29.214-f10] */
const val AC_SPECIFIC_APN_INFO                                  = 1472L               /* [29.272-f10] */
const val AC_SPONSOR_IDENTITY                                   = 531L                /* [29.214-f10] */
const val AC_SPONSORED_CONNECTIVITY_DATA                        = 530L                /* [29.214-f10] */
const val AC_SPONSORING_ACTION                                  = 542L                /* [29.214-f10] */
const val AC_SRES                                               = 1454L               /* [29.272-f10] */
const val AC_SRR_FLAGS                                          = 3310L               /* [29.338-f00] */
const val AC_SS_CODE                                            = 1476L               /* [29.272-f10] */
const val AC_SS_STATUS                                          = 1477L               /* [29.272-f10] */
const val AC_SSID                                               = 1524L               /* [29.273-f10] */
const val AC_START_TIME                                         = 2041L               /* [32-299-f10] */
const val AC_STATE                                              = 24L                 /* [RFC 7155] */
const val AC_STATIONARY_INDICATION                              = 3119L               /* [29.336-f10] */
const val AC_STN_SR                                             = 1433L               /* [29.272-f10] */
const val AC_STOP_TIME                                          = 2042L               /* [32-299-f10] */
const val AC_SUBMISSION_TIME                                    = 1202L               /* [32-299-f10] */
const val AC_SUBS_REQ_TYPE                                      = 705L                /* [29.329-f00] */
const val AC_SUBSCRIBED_PERIODIC_RAU_TAU_TIMER                  = 1619L               /* [29.272-f10] */
const val AC_SUBSCRIBED_VSRVCC                                  = 1636L               /* [29.272-f10] */
const val AC_SUBSCRIBER_ROLE                                    = 2033L               /* [32-299-f10] */
const val AC_SUBSCRIBER_STATUS                                  = 1424L               /* [29.272-f10] */
const val AC_SUBSCRIPTION_DATA                                  = 1400L               /* [29.272-f10] */
const val AC_SUBSCRIPTION_DATA_DELETION                         = 1685L               /* [29.272-f10] */
const val AC_SUBSCRIPTION_DATA_FLAGS                            = 16454L              /* [29.272-f10] */
const val AC_SUBSCRIPTION_ID                                    = 443L                /* [RFC 4006] */
const val AC_SUBSCRIPTION_ID_DATA                               = 444L                /* [RFC 4006] */
const val AC_SUBSCRIPTION_ID_TYPE                               = 450L                /* [RFC 4006] */
const val AC_SUBSCRIPTION_INFO                                  = 642L                /* [29.229-e20] */
const val AC_SUBSESSION_DECISION_INFO                           = 2200L               /* [29.215-f00] */
const val AC_SUBSESSION_ENFORCEMENT_INFO                        = 2201L               /* [29.215-f00] */
const val AC_SUBSESSION_ID                                      = 2202L               /* [29.215-f00] */
const val AC_SUBSESSION_OPERATION                               = 2203L               /* [29.215-f00] */
const val AC_SUGGESTED_NETWORK_CONFIGURATION                    = 3170L               /* [29.336-f10] */
const val AC_SUPPLEMENTARY_SERVICE                              = 2048L               /* [32-299-f10] */
const val AC_SUPPORTED_APPLICATIONS                             = 631L                /* [29.229-e20] */
const val AC_SUPPORTED_FEATURES                                 = 628L                /* [29.229-e20] */
const val AC_SUPPORTED_FEATURES_S6A                             = 628L                /* [29.272-f10] */
const val AC_SUPPORTED_MONITORING_EVENTS                        = 3144L               /* [29.336-f10] */
const val AC_SUPPORTED_MONITORING_EVENTS_S6                     = 3144L               /* [29.272-f10] */
const val AC_SUPPORTED_SERVICES                                 = 3143L               /* [29.336-f10] */
const val AC_SUPPORTED_SERVICES_S6                              = 3143L               /* [29.272-f10] */
const val AC_SUPPORTED_VENDOR_ID                                = 265L                /* [RFC 6733] */
const val AC_T4_DATA                                            = 3108L               /* [29.336-f10] */
const val AC_T4_PARAMETERS                                      = 3106L               /* [29.336-f10] */
const val AC_TALK_BURST_EXCHANGE                                = 1255L               /* [32-299-f10] */
const val AC_TALK_BURST_TIME                                    = 1286L               /* [32-299-f10] */
const val AC_TALK_BURST_VOLUME                                  = 1287L               /* [32-299-f10] */
const val AC_TARIFF_CHANGE_USAGE                                = 452L                /* [RFC 4006] */
const val AC_TARIFF_INFORMATION                                 = 2060L               /* [32-299-f10] */
const val AC_TARIFF_TIME_CHANGE                                 = 451L                /* [RFC 4006] */
const val AC_TARIFF_XML                                         = 2306L               /* [32-299-f10] */
const val AC_TCP_FLAG_TYPE                                      = 544L                /* [RFC 5777] */
const val AC_TCP_FLAGS                                          = 543L                /* [RFC 5777] */
const val AC_TCP_OPTION                                         = 540L                /* [RFC 5777] */
const val AC_TCP_OPTION_TYPE                                    = 541L                /* [RFC 5777] */
const val AC_TCP_OPTION_VALUE                                   = 542L                /* [RFC 5777] */
const val AC_TCP_SOURCE_PORT                                    = 2843L               /* [29.212-f10] */
const val AC_TDA_FLAGS                                          = 4321L               /* [29.128-f00] */
const val AC_TDF_APPLICATION_IDENTIFIER                         = 1088L               /* [29.212-f10] */
const val AC_TDF_APPLICATION_INSTANCE_IDENTIFIER                = 2802L               /* [29.212-f10] */
const val AC_TDF_DESTINATION_HOST                               = 1089L               /* [29.212-f10] */
const val AC_TDF_DESTINATION_REALM                              = 1090L               /* [29.212-f10] */
const val AC_TDF_INFORMATION                                    = 1087L               /* [29.212-f10] */
const val AC_TDF_IP_ADDRESS                                     = 1091L               /* [29.212-f10] */
const val AC_TELESERVICE_LIST                                   = 1486L               /* [29.272-f10] */
const val AC_TERMINAL_INFORMATION                               = 1401L               /* [29.272-f10] */
const val AC_TERMINAL_INFORMATION_T6                            = 1401L               /* [29.128-f00] */
const val AC_TERMINATING_IOI                                    = 840L                /* [32-299-f10] */
const val AC_TERMINATION_CAUSE                                  = 295L                /* [RFC 6733] */
const val AC_TERMINATION_CAUSE_SLG                              = 2548L               /* [29.172-d10] */
const val AC_TFR_FLAGS                                          = 3302L               /* [29.338-f00] */
const val AC_TFT_FILTER                                         = 1012L               /* [29.212-f10] */
const val AC_TFT_PACKET_FILTER_INFORMATION                      = 1013L               /* [29.212-f10] */
const val AC_TIME_FIRST_USAGE                                   = 2043L               /* [32-299-f10] */
const val AC_TIME_LAST_USAGE                                    = 2044L               /* [32-299-f10] */
const val AC_TIME_OF_DAY_CONDITION                              = 560L                /* [RFC 5777] */
const val AC_TIME_OF_DAY_END                                    = 562L                /* [RFC 5777] */
const val AC_TIME_OF_DAY_START                                  = 561L                /* [RFC 5777] */
const val AC_TIME_QUOTA_MECHANISM                               = 1270L               /* [32-299-f10] */
const val AC_TIME_QUOTA_THRESHOLD                               = 868L                /* [32-299-f10] */
const val AC_TIME_QUOTA_TYPE                                    = 1271L               /* [32-299-f10] */
const val AC_TIME_STAMPS                                        = 833L                /* [32-299-f10] */
const val AC_TIME_USAGE                                         = 2045L               /* [32-299-f10] */
const val AC_TIME_WINDOW                                        = 4204L               /* [29.154-f00] */
const val AC_TIME_ZONE                                          = 1642L               /* [29.272-f10] */
const val AC_TIMEZONE_FLAG                                      = 570L                /* [RFC 5777] */
const val AC_TIMEZONE_OFFSET                                    = 571L                /* [RFC 5777] */
const val AC_TMGI                                               = 900L                /* [29.061-f10] */
const val AC_TO_SIP_HEADER                                      = 645L                /* [29.229-e20] */
const val AC_TO_SPEC                                            = 516L                /* [RFC 5777] */
const val AC_TOKEN_TEXT                                         = 1215L               /* [32-299-f10] */
const val AC_TOS_TRAFFIC_CLASS                                  = 1014L               /* [29.212-f10] */
const val AC_TOTAL_NUMBER_OF_MESSAGES_EXPLODED                  = 2113L               /* [OMADDSChargingData] */
const val AC_TOTAL_NUMBER_OF_MESSAGES_SENT                      = 2114L               /* [OMADDSChargingData] */
const val AC_TRACE_COLLECTION_ENTITY                            = 1452L               /* [29.272-f10] */
const val AC_TRACE_DATA                                         = 1458L               /* [29.272-f10] */
const val AC_TRACE_DEPTH                                        = 1462L               /* [32.422] */
const val AC_TRACE_EVENT_LIST                                   = 1465L               /* [32.422] */
const val AC_TRACE_INFO                                         = 1505L               /* [29.273-f10] */
const val AC_TRACE_INTERFACE_LIST                               = 1464L               /* [32.422] */
const val AC_TRACE_NE_TYPE_LIST                                 = 1463L               /* [32.422] */
const val AC_TRACE_REFERENCE                                    = 1459L               /* [29.272-f10] */
const val AC_TRACKING_AREA_IDENTITY                             = 1603L               /* [29.272-f10] */
const val AC_TRAFFIC_DATA_VOLUMES                               = 2046L               /* [32-299-f10] */
const val AC_TRAFFIC_STEERING_POLICY_IDENTIFIER_DL              = 2836L               /* [29.212-f10] */
const val AC_TRAFFIC_STEERING_POLICY_IDENTIFIER_UL              = 2837L               /* [29.212-f10] */
const val AC_TRANSCODER_INSERTED_INDICATION                     = 2605L               /* [32-299-f10] */
const val AC_TRANSFER_END_TIME                                  = 4205L               /* [29.154-f00] */
const val AC_TRANSFER_POLICY                                    = 4207L               /* [29.154-f00] */
const val AC_TRANSFER_POLICY_ID                                 = 4208L               /* [29.154-f00] */
const val AC_TRANSFER_REQUEST_TYPE                              = 4203L               /* [29.154-f00] */
const val AC_TRANSFER_START_TIME                                = 4206L               /* [29.154-f00] */
const val AC_TRANSPORT_ACCESS_TYPE                              = 1519L               /* [29.273-f10] */
const val AC_TREATMENT_ACTION                                   = 572L                /* [RFC 5777] */
const val AC_TRIGGER                                            = 1264L               /* [32-299-f10] */
const val AC_TRIGGER_ACTION                                     = 3202L               /* [29.337-e10] */
const val AC_TRIGGER_DATA                                       = 3003L               /* [29.368-e20] */
const val AC_TRIGGER_TYPE                                       = 870L                /* [32-299-f10] */
const val AC_TRUNK_GROUP_ID                                     = 851L                /* [32-299-f10] */
const val AC_TS_CODE                                            = 1487L               /* [29.272-f10] */
const val AC_TUNNEL_ASSIGNMENT_ID                               = 82L                 /* [RFC 7155] */
const val AC_TUNNEL_CLIENT_AUTH_ID                              = 90L                 /* [RFC 7155] */
const val AC_TUNNEL_CLIENT_ENDPOINT                             = 66L                 /* [RFC 7155] */
const val AC_TUNNEL_HEADER_FILTER                               = 1036L               /* [29.212-f10] */
const val AC_TUNNEL_HEADER_LENGTH                               = 1037L               /* [29.212-f10] */
const val AC_TUNNEL_INFORMATION                                 = 1038L               /* [29.212-f10] */
const val AC_TUNNEL_MEDIUM_TYPE                                 = 65L                 /* [RFC 7155] */
const val AC_TUNNEL_PASSWORD                                    = 69L                 /* [RFC 7155] */
const val AC_TUNNEL_PREFERENCE                                  = 83L                 /* [RFC 7155] */
const val AC_TUNNEL_PRIVATE_GROUP_ID                            = 81L                 /* [RFC 7155] */
const val AC_TUNNEL_SERVER_AUTH_ID                              = 91L                 /* [RFC 7155] */
const val AC_TUNNEL_SERVER_ENDPOINT                             = 67L                 /* [RFC 7155] */
const val AC_TUNNEL_TYPE                                        = 64L                 /* [RFC 7155] */
const val AC_TUNNELING                                          = 401L                /* [RFC 7155] */
const val AC_TWAG_ADDRESS                                       = 3903L               /* [32-299-f10] */
const val AC_TWAG_CP_ADDRESS                                    = 1531L               /* [29.273-f10] */
const val AC_TWAG_UP_ADDRESS                                    = 1532L               /* [29.273-f10] */
const val AC_TWAN_ACCESS_INFO                                   = 1510L               /* [29.273-f10] */
const val AC_TWAN_CONNECTION_MODE                               = 1527L               /* [29.273-f10] */
const val AC_TWAN_CONNECTIVITY_PARAMETERS                       = 1528L               /* [29.273-f10] */
const val AC_TWAN_DEFAULT_APN_CONTEXT_ID                        = 1512L               /* [29.273-f10] */
const val AC_TWAN_IDENTIFIER                                    = 29L                 /* [29.061-f10] */
const val AC_TWAN_PCO                                           = 1530L               /* [29.273-f10] */
const val AC_TWAN_S2A_FAILURE_CAUSE                             = 1533L               /* [29.273-f10] */
const val AC_TWAN_USER_LOCATION_INFO                            = 2714L               /* [32-299-f10] */
const val AC_TYPE_NUMBER                                        = 1204L               /* [32-299-f10] */
const val AC_TYPE_OF_EXTERNAL_IDENTIFIER                        = 3168L               /* [29.336-f10] */
const val AC_UAR_FLAGS                                          = 637L                /* [29.229-e20] */
const val AC_UDP_SOURCE_PORT                                    = 2806L               /* [29.212-f10] */
const val AC_UDR_FLAGS                                          = 719L                /* [29.329-f00] */
const val AC_UE_COUNT                                           = 4308L               /* [29.128-f10] */
const val AC_UE_LOCAL_IP_ADDRESS                                = 2805L               /* [29.212-f10] */
const val AC_UE_PC5_AMBR                                        = 1693L               /* [29.272-f10] */
const val AC_UE_REACHABILITY_CONFIGURATION                      = 3129L               /* [29.336-f10] */
const val AC_UE_REACHABILITY_CONFIGURATION_S6                   = 3129L               /* [29.272-f10] */
const val AC_UE_SRVCC_CAPABILITY                                = 1615L               /* [29.272-f10] */
const val AC_UE_USAGE_TYPE                                      = 1680L               /* [29.272-f10] */
const val AC_ULA_FLAGS                                          = 1406L               /* [29.272-f10] */
const val AC_ULR_FLAGS                                          = 1405L               /* [29.272-f10] */
const val AC_UNI_PDU_CP_ONLY_FLAG                               = 3932L               /* [32-299-f10] */
const val AC_UNIT_COST                                          = 2061L               /* [32-299-f10] */
const val AC_UNIT_QUOTA_THRESHOLD                               = 1226L               /* [32-299-f10] */
const val AC_UNIT_VALUE                                         = 445L                /* [RFC 4006] */
const val AC_UNUSED_QUOTA_TIMER                                 = 4407L               /* [32-299-f10] */
const val AC_UPLINK_RATE_LIMIT                                  = 4311L               /* [29.128-f10] */
const val AC_USAGE_MONITORING_INFORMATION                       = 1067L               /* [29.212-f10] */
const val AC_USAGE_MONITORING_LEVEL                             = 1068L               /* [29.212-f10] */
const val AC_USAGE_MONITORING_REPORT                            = 1069L               /* [29.212-f10] */
const val AC_USAGE_MONITORING_SUPPORT                           = 1070L               /* [29.212-f10] */
const val AC_USE_ASSIGNED_ADDRESS                               = 534L                /* [RFC 5777] */
const val AC_USED_SERVICE_UNIT                                  = 446L                /* [RFC 4006] */
const val AC_USED_SERVICE_UNIT_32299                            = 446L                /* [32-299-f10] */
const val AC_USER_AUTHORIZATION_TYPE                            = 623L                /* [29.229-e20] */
const val AC_USER_CSG_INFORMATION                               = 2319L               /* [32-299-f10] */
const val AC_USER_CSG_INFORMATION_S6                            = 2319L               /* [29.272-f10] */
const val AC_USER_DATA                                          = 606L                /* [29.229-e20] */
const val AC_USER_DATA_ALREADY_AVAILABLE                        = 624L                /* [29.229-e20] */
const val AC_USER_DATA_SH                                       = 702L                /* [29.329-f00] */
const val AC_USER_EQUIPMENT_INFO                                = 458L                /* [RFC 4006] */
const val AC_USER_EQUIPMENT_INFO_TYPE                           = 459L                /* [RFC 4006] */
const val AC_USER_EQUIPMENT_INFO_VALUE                          = 460L                /* [RFC 4006] */
const val AC_USER_ID                                            = 1444L               /* [29.272-f10] */
const val AC_USER_IDENTIFIER                                    = 3102L               /* [29.336-f10] */
const val AC_USER_IDENTIFIER_S6T                                = 3102L               /* [29.336-f10] */
const val AC_USER_IDENTIFIER_T6                                 = 3102L               /* [29.128-f10] */
const val AC_USER_IDENTITY                                      = 700L                /* [29.329-f00] */
const val AC_USER_LOCATION_INFO_TIME                            = 2812L               /* [29.212-f10] */
const val AC_USER_NAME                                          = 1L                  /* [RFC 6733] */
const val AC_USER_PARTICIPATING_TYPE                            = 1279L               /* [32-299-f10] */
const val AC_USER_PASSWORD                                      = 2L                  /* [RFC 7155] */
const val AC_USER_PRIORITY_RANGE                                = 557L                /* [RFC 5777] */
const val AC_USER_SESSION_ID                                    = 830L                /* [32-299-f10] */
const val AC_USER_STATE                                         = 1499L               /* [29.272-f10] */
const val AC_UTRAN_ADDITIONAL_POSITIONING_DATA                  = 2558L               /* [29.172-d10] */
const val AC_UTRAN_GANSS_POSTIONING_DATA                        = 2529L               /* [29.172-d10] */
const val AC_UTRAN_POSITIONING_INFO                             = 2527L               /* [29.172-d10] */
const val AC_UTRAN_POSTIONING_DATA                              = 2528L               /* [29.172-d10] */
const val AC_UTRAN_VECTOR                                       = 1415L               /* [29.272-f10] */
const val AC_UVA_FLAGS                                          = 1640L               /* [29.272-f10] */
const val AC_UVR_FLAGS                                          = 1639L               /* [29.272-f10] */
const val AC_UWAN_USER_LOCATION_INFO                            = 3918L               /* [32-299-f10] */
const val AC_V2X_PERMISSION                                     = 1689L               /* [29.272-f10] */
const val AC_V2X_SUBSCRIPTION_DATA                              = 1688L               /* [29.272-f10] */
const val AC_VALIDITY_TIME                                      = 448L                /* [RFC 4006] */
const val AC_VALUE_DIGITS                                       = 447L                /* [RFC 4006] */
const val AC_VAS_ID                                             = 1102L               /* [29.140-700] */
const val AC_VASP_ID                                            = 1101L               /* [29.140-700] */
const val AC_VELOCITY_ESTIMATE                                  = 2515L               /* [29.172-d10] */
const val AC_VELOCITY_REQUESTED                                 = 2508L               /* [29.172-d10] */
const val AC_VENDOR_ID                                          = 266L                /* [RFC 6733] */
const val AC_VENDOR_SPECIFIC_APPLICATION_ID                     = 260L                /* [RFC 6733] */
const val AC_VERTICAL_ACCURACY                                  = 2506L               /* [29.172-d10] */
const val AC_VERTICAL_REQUESTED                                 = 2507L               /* [29.172-d10] */
const val AC_VISITED_NETWORK_IDENTIFIER                         = 600L                /* [29.229-e20] */
const val AC_VISITED_NETWORK_IDENTIFIER_S6                      = 600L                /* [29.272-f10] */
const val AC_VISITED_PLMN_ID                                    = 1407L               /* [29.272-f10] */
const val AC_VLAN_ID_RANGE                                      = 552L                /* [RFC 5777] */
const val AC_VOLUME_QUOTA_THRESHOLD                             = 869L                /* [32-299-f10] */
const val AC_VPLMN_CSG_SUBSCRIPTION_DATA                        = 1641L               /* [29.272-f10] */
const val AC_VPLMN_DYNAMIC_ADDRESS_ALLOWED                      = 1432L               /* [29.272-f10] */
const val AC_VPLMN_LIPA_ALLOWED                                 = 1617L               /* [29.272-f10] */
const val AC_WAG_ADDRESS                                        = 890L                /* [32-299-f10] */
const val AC_WAG_PLMN_ID                                        = 891L                /* [32-299-f10] */
const val AC_WEBRTC_AUTHENTICATION_FUNCTION_NAME                = 657L                /* [29.229-e20] */
const val AC_WEBRTC_WEB_SERVER_FUNCTION_NAME                    = 658L                /* [29.229-e20] */
const val AC_WILDCARDED_IMPU                                    = 636L                /* [29.329-f00] */
const val AC_WILDCARDED_PUBLIC_IDENTITY                         = 634L                /* [29.229-e20] */
const val AC_WLAN_3GPP_IP_ACCESS                                = 306L                /* [29.234-b20] */
const val AC_WLAN_ACCESS                                        = 305L                /* [29.234-b20] */
const val AC_WLAN_DIRECT_IP_ACCESS                              = 310L                /* [29.234-b20] */
const val AC_WLAN_IDENTIFIER                                    = 1509L               /* [29.273-f10] */
const val AC_WLAN_INFORMATION                                   = 875L                /* [32-299-f10] */
const val AC_WLAN_OFFLOADABILITY                                = 1667L               /* [29.272-f10] */
const val AC_WLAN_OFFLOADABILITY_EUTRAN                         = 1668L               /* [29.272-f10] */
const val AC_WLAN_OFFLOADABILITY_UTRAN                          = 1669L               /* [29.272-f10] */
const val AC_WLAN_RADIO_CONTAINER                               = 892L                /* [32-299-f10] */
const val AC_WLAN_SESSION_ID                                    = 1246L               /* [32-299-f10] */
const val AC_WLAN_TECHNOLOGY                                    = 893L                /* [32-299-f10] */
const val AC_WLAN_UE_LOCAL_IPADDRESS                            = 894L                /* [32-299-f10] */
const val AC_WLAN_USER_DATA                                     = 303L                /* [29.234-b20] */
const val AC_WLCP_KEY                                           = 1535L               /* [29.273-f10] */
const val AC_XRES                                               = 1448L               /* [29.272-f10] */

/* Redirect-Host-Usage AVP Values (code 261) */
const val RHU_DONT_CACHE                                        = 0                   /* [RFC6733] */
const val RHU_ALL_SESSION                                       = 1                   /* [RFC6733] */
const val RHU_ALL_REALM                                         = 2                   /* [RFC6733] */
const val RHU_REALM_AND_APPLICATION                             = 3                   /* [RFC6733] */
const val RHU_ALL_APPLICATION                                   = 4                   /* [RFC6733] */
const val RHU_ALL_HOST                                          = 5                   /* [RFC6733] */
const val RHU_ALL_USER                                          = 6                   /* [RFC6733] */

/* Result-Code AVP Values (code 268) - Informational */
const val RESULT_DIAMETER_MULTI_ROUND_AUTH                      = 1001                /* [RFC6733] */

/* Result-Code AVP Values (code 268) - Success */
const val RC_DIAMETER_SUCCESS                                   = 2001L               /* [RFC6733] */
const val RC_DIAMETER_LIMITED_SUCCESS                           = 2002L               /* [RFC6733] */
const val RC_DIAMETER_FIRST_REGISTRATION                        = 2003L               /* [RFC4740] */
const val RC_DIAMETER_SUBSEQUENT_REGISTRATION                   = 2004L               /* [RFC4740] */
const val RC_DIAMETER_UNREGISTERED_SERVICE                      = 2005L               /* [RFC4740] */
const val RC_DIAMETER_SUCCESS_SERVER_NAME_NOT_STORED            = 2006L               /* [RFC4740] */
const val RC_DIAMETER_SERVER_SELECTION                          = 2007L               /* [RFC4740] */
const val RC_DIAMETER_SUCCESS_AUTH_SENT_SERVER_NOT_STORED       = 2008L               /* [RFC4740] */
const val RC_DIAMETER_SUCCESS_RELOCATE_HA                       = 2009L               /* [RFC5778] */

/* Result-Code AVP Values (code 268) - Protocol Errors */
const val RC_DIAMETER_COMMAND_UNSUPPORTED                       = 3001                /* [RFC6733] */
const val RC_DIAMETER_UNABLE_TO_DELIVER                         = 3002                /* [RFC6733] */
const val RC_DIAMETER_REALM_NOT_SERVED                          = 3003                /* [RFC6733] */
const val RC_DIAMETER_DIAMETER_TOO_BUSY                         = 3004                /* [RFC6733] */
const val RC_DIAMETER_LOOP_DETECTED                             = 3005                /* [RFC6733] */
const val RC_DIAMETER_REDIRECT_INDICATION                       = 3006                /* [RFC6733] */
const val RC_DIAMETER_APPLICATION_UNSUPPORTED                   = 3007                /* [RFC6733] */
const val RC_DIAMETER_INVALID_HDR_BITS                          = 3008                /* [RFC6733] */
const val RC_DIAMETER_INVALID_AVP_BITS                          = 3009                /* [RFC6733] */
const val RC_DIAMETER_UNKNOWN_PEER                              = 3010                /* [RFC6733] */
const val RC_DIAMETER_REALM_REDIRECT_INDICATION                 = 3011                /* [RFC7075] */

/* Result-Code AVP Values (code 268) - Transient Failures */
const val RC_DIAMETER_AUTHENTICATION_REJECTED                   = 4001                /* [RFC6733] */
const val RC_DIAMETER_OUT_OF_SPACE                              = 4002                /* [RFC6733] */
const val RC_ELECTION_LOST                                      = 4003                /* [RFC6733] */
const val RC_DIAMETER_ERROR_MIP_REPLY_FAILURE                   = 4005                /* [RFC4004] */
const val RC_DIAMETER_ERROR_HA_NOT_AVAILABLE                    = 4006                /* [RFC4004] */
const val RC_DIAMETER_ERROR_BAD_KEY                             = 4007                /* [RFC4004] */
const val RC_DIAMETER_ERROR_MIP_FILTER_NOT_SUPPORTED            = 4008                /* [RFC4004] */
const val RC_DIAMETER_END_USER_SERVICE_DENIED                   = 4010                /* [RFC4006] */
const val RC_DIAMETER_CREDIT_CONTROL_NOT_APPLICABLE             = 4011                /* [RFC4006] */
const val RC_DIAMETER_CREDIT_LIMIT_REACHED                      = 4012                /* [RFC4006] */
const val RC_DIAMETER_USER_NAME_REQUIRED                        = 4013                /* [RFC4740] */
const val RC_DIAMETER_RESOURCE_FAILURE                          = 4014                /* [RFC6736] */

/* Result-Code AVP Values (code 268) - Permanent Failure */
const val RC_DIAMETER_AVP_UNSUPPORTED                           = 5001                /* [RFC6733] */
const val RC_DIAMETER_UNKNOWN_SESSION_ID                        = 5002                /* [RFC6733] */
const val RC_DIAMETER_AUTHORIZATION_REJECTED                    = 5003                /* [RFC6733] */
const val RC_DIAMETER_INVALID_AVP_VALUE                         = 5004                /* [RFC6733] */
const val RC_DIAMETER_MISSING_AVP                               = 5005                /* [RFC6733] */
const val RC_DIAMETER_RESOURCES_EXCEEDED                        = 5006                /* [RFC6733] */
const val RC_DIAMETER_CONTRADICTING_AVPS                        = 5007                /* [RFC6733] */
const val RC_DIAMETER_AVP_NOT_ALLOWED                           = 5008                /* [RFC6733] */
const val RC_DIAMETER_AVP_OCCURS_TOO_MANY_TIMES                 = 5009                /* [RFC6733] */
const val RC_DIAMETER_NO_COMMON_APPLICATION                     = 5010                /* [RFC6733] */
const val RC_DIAMETER_UNSUPPORTED_VERSION                       = 5011                /* [RFC6733] */
const val RC_DIAMETER_UNABLE_TO_COMPLY                          = 5012                /* [RFC6733] */
const val RC_DIAMETER_INVALID_BIT_IN_HEADER                     = 5013                /* [RFC6733] */
const val RC_DIAMETER_INVALID_AVP_LENGTH                        = 5014                /* [RFC6733] */
const val RC_DIAMETER_INVALID_MESSAGE_LENGTH                    = 5015                /* [RFC6733] */
const val RC_DIAMETER_INVALID_AVP_BIT_COMBO                     = 5016                /* [RFC6733] */
const val RC_DIAMETER_NO_COMMON_SECURITY                        = 5017                /* [RFC6733] */
const val RC_DIAMETER_RADIUS_AVP_UNTRANSLATABLE                 = 5018                /* [RFC4849] */
const val RC_DIAMETER_ERROR_NO_FOREIGN_HA_SERVICE               = 5024                /* [RFC4004] */
const val RC_DIAMETER_ERROR_END_TO_END_MIP_KEY_ENCRYPTION       = 5025                /* [RFC4004] */
const val RC_DIAMETER_USER_UNKNOWN                              = 5030                /* [RFC4006] */
const val RC_DIAMETER_RATING_FAILED                             = 5031                /* [RFC4006] */
const val RC_DIAMETER_ERROR_USER_UNKNOWN                        = 5032                /* [RFC4740] */
const val RC_DIAMETER_ERROR_IDENTITIES_DONT_MATCH               = 5033                /* [RFC4740] */
const val RC_DIAMETER_ERROR_IDENTITY_NOT_REGISTERED             = 5034                /* [RFC4740] */
const val RC_DIAMETER_ERROR_ROAMING_NOT_ALLOWED                 = 5035                /* [RFC4740] */
const val RC_DIAMETER_ERROR_IDENTITY_ALREADY_REGISTERED         = 5036                /* [RFC4740] */
const val RC_DIAMETER_ERROR_AUTH_SCHEME_NOT_SUPPORTED           = 5037                /* [RFC4740] */
const val RC_DIAMETER_ERROR_IN_ASSIGNMENT_TYPE                  = 5038                /* [RFC4740] */
const val RC_DIAMETER_ERROR_TOO_MUCH_DATA                       = 5039                /* [RFC4740] */
const val RC_DIAMETER_ERROR_NOT_SUPPORTED_USER_DATA             = 5040                /* [RFC4740] */
const val RC_DIAMETER_ERROR_MIP6_AUTH_MODE                      = 5041                /* [RFC5778] */
const val RC_DIAMETER_UNKNOWN_BINDING_TEMPLATE_NAME             = 5042                /* [RFC6736] */
const val RC_DIAMETER_BINDING_FAILURE                           = 5043                /* [RFC6736] */
const val RC_DIAMETER_MAX_BINDING_SET_FAILURE                   = 5044                /* [RFC6736] */
const val RC_DIAMETER_MAXIMUM_BINDINGS_REACHED_FOR_ENDPOINT     = 5045                /* [RFC6736] */
const val RC_DIAMETER_SESSION_EXISTS                            = 5046                /* [RFC6736] */
const val RC_DIAMETER_INSUFFICIENT_CLASSIFIERS                  = 5047                /* [RFC6736] */
const val RC_DIAMETER_ERROR_EAP_CODE_UNKNOWN                    = 5048                /* [RFC6942] */

/* Session-Binding AVP Values (code 270) */
const val SESSION_BINDING_RE_AUTH                               = 1                   /* [RFC6733] */
const val SESSION_BINDING_STR                                   = 2                   /* [RFC6733] */
const val SESSION_BINDING_ACCOUNTING                            = 4                   /* [RFC6733] */

/* Session-Server-Failover AVP Values (code 271) */
const val SSF_REFUSE_SERVICE                                    = 0                   /* [RFC6733] */
const val SSF_TRY_AGAIN                                         = 1                   /* [RFC6733] */
const val SSF_ALLOW_SERVICE                                     = 2                   /* [RFC6733] */
const val SSF_TRY_AGAIN_ALLOW_SERVICE                           = 3                   /* [RFC6733] */

/* Disconnect-Cause AVP Values (code 273) */
const val DC_REBOOTING                                          = 0                   /* [RFC6733] */
const val DC_BUSY                                               = 1                   /* [RFC6733] */
const val DC_DO_NOT_WANT_TO_TALK_TO_YOU                         = 2                   /* [RFC6733] */

/* Auth-Request-Type AVP Values (code 274) */
const val ART_AUTHENTICATE_ONLY                                 = 1                   /* [RFC6733] */
const val ART_AUTHORIZE_ONLY                                    = 2                   /* [RFC6733] */
const val ART_AUTHORIZE_AUTHENTICATE                            = 3                   /* [RFC6733] */

/* Auth-Session-State AVP Values (code 277) */
const val ASS_STATE_MAINTAINED                                  = 0                   /* [RFC6733] */
const val ASS_NO_STATE_MAINTAINED                               = 1                   /* [RFC6733] */

/* Re-Auth-Request-Type AVP Values (code 285) */
const val RART_AUTHORIZE_ONLY                                   = 0                   /* [RFC6733] */
const val RART_AUTHORIZE_AUTHENTICATE                           = 1                   /* [RFC6733] */

/* Termination-Cause AVP Values (code 295) */
const val TC_DIAMETER_LOGOUT                                    = 1                   /* [RFC3588andRFC6733] */
const val TC_DIAMETER_SERVICE_NOT_PROVIDED                      = 2                   /* [RFC3588andRFC6733] */
const val TC_DIAMETER_BAD_ANSWER                                = 3                   /* [RFC3588andRFC6733] */
const val TC_DIAMETER_ADMINISTRATIVE                            = 4                   /* [RFC3588andRFC6733] */
const val TC_DIAMETER_LINK_BROKEN                               = 5                   /* [RFC3588andRFC6733] */
const val TC_DIAMETER_AUTH_EXPIRED                              = 6                   /* [RFC3588andRFC6733] */
const val TC_DIAMETER_USER_MOVED                                = 7                   /* [RFC3588andRFC6733] */
const val TC_DIAMETER_SESSION_TIMEOUT                           = 8                   /* [RFC3588andRFC6733] */
const val TC_USER_REQUEST                                       = 11                  /* [RFC2866andRFC7155] */
const val TC_LOST_CARRIER                                       = 12                  /* [RFC2866andRFC7155] */
const val TC_LOST_SERVICE                                       = 13                  /* [RFC2866andRFC7155] */
const val TC_IDLE_TIMEOUT                                       = 14                  /* [RFC2866andRFC7155] */
const val TC_SESSION_TIMEOUT                                    = 15                  /* [RFC2866andRFC7155] */
const val TC_ADMIN_RESET                                        = 16                  /* [RFC2866andRFC7155] */
const val TC_ADMIN_REBOOT                                       = 17                  /* [RFC2866andRFC7155] */
const val TC_PORT_ERROR                                         = 18                  /* [RFC2866andRFC7155] */
const val TC_NAS_ERROR                                          = 19                  /* [RFC2866andRFC7155] */
const val TC_NAS_REQUEST                                        = 20                  /* [RFC2866andRFC7155] */
const val TC_NAS_REBOOT                                         = 21                  /* [RFC2866andRFC7155] */
const val TC_PORT_UNNEEDED                                      = 22                  /* [RFC2866andRFC7155] */
const val TC_PORT_PREEMPTED                                     = 23                  /* [RFC2866andRFC7155] */
const val TC_PORT_SUSPENDED                                     = 24                  /* [RFC2866andRFC7155] */
const val TC_SERVICE_UNAVAILABLE                                = 25                  /* [RFC2866andRFC7155] */
const val TC_CALLBACK                                           = 26                  /* [RFC2866andRFC7155] */
const val TC_USER_ERROR                                         = 27                  /* [RFC2866andRFC7155] */
const val TC_HOST_REQUEST                                       = 28                  /* [RFC2866andRFC7155] */
const val TC_SUPPLICANT_RESTART                                 = 29                  /* [RFC3580andRFC7155] */
const val TC_REAUTHENTICATION_FAILURE                           = 30                  /* [RFC3580andRFC7155] */
const val TC_PORT_REINITIALIZED                                 = 31                  /* [RFC3580andRFC7155] */
const val TC_PORT_ADMINISTRATIVELY_DISABLED                     = 32                  /* [RFC3580andRFC7155] */

/* Inband-Security-Id AVP (code 299) */
const val ISI_NO_INBAND_SECURITY                                = 0                   /* [RFC6733] */
const val ISI_TLS                                               = 1                   /* [RFC6733] */

/* MIP-Feature-Vector AVP (code 337) */
const val MIP_MOBILE_NODE_HOME_ADDRESS_REQUESTED                = 1                   /* [RFC4004] */
const val MIP_HOME_ADDRESS_ALLOCATABLE_ONLY_IN_HOME_REALM       = 2                   /* [RFC4004] */
const val MIP_HOME_AGENT_REQUESTED                              = 4                   /* [RFC4004] */
const val MIP_FOREIGN_HOME_AGENT_AVAILABLE                      = 8                   /* [RFC4004] */
const val MIP_MN_HA_KEY_REQUESTED                               = 16                  /* [RFC4004] */
const val MIP_MN_FA_KEY_REQUESTED                               = 32                  /* [RFC4004] */
const val MIP_FA_HA_KEY_REQUESTED                               = 64                  /* [RFC4004] */
const val MIP_HOME_AGENT_IN_FOREIGN_NETWORK                     = 128                 /* [RFC4004] */
const val MIP_CO_LOCATED_MOBILE_NODE                            = 256                 /* [RFC4004] */

/* MIP-Algorithm-Type AVP Values (code 345) */
const val MIP_HMAC_SHA_1                                        = 2                   /* [RFC4004] */

/* MIP-Replay-Mode AVP Values (code 346) */
const val MIP_NONE                                              = 1                   /* [RFC4004] */
const val MIP_TIMESTAMPS                                        = 2                   /* [RFC4004] */
const val MIP_NONCES                                            = 3                   /* [RFC4004] */

/* SIP-Server-Assignment-Type AVP Values (375) */
const val SIP_SA_NO_ASSIGNMENT                                  = 0                   /* [RFC4740] */
const val SIP_SA_REGISTRATION                                   = 1                   /* [RFC4740] */
const val SIP_SA_RE_REGISTRATION                                = 2                   /* [RFC4740] */
const val SIP_SA_UNREGISTERED_USER                              = 3                   /* [RFC4740] */
const val SIP_SA_TIMEOUT_DEREGISTRATION                         = 4                   /* [RFC4740] */
const val SIP_SA_USER_DEREGISTRATION                            = 5                   /* [RFC4740] */
const val SIP_SA_TIMEOUT_DEREGISTRATION_STORE_SERVER_NAME       = 6                   /* [RFC4740] */
const val SIP_SA_USER_DEREGISTRATION_STORE_SERVER_NAME          = 7                   /* [RFC4740] */
const val SIP_SA_ADMINISTRATIVE_DEREGISTRATION                  = 8                   /* [RFC4740] */
const val SIP_SA_AUTHENTICATION_FAILURE                         = 9                   /* [RFC4740] */
const val SIP_SA_AUTHENTICATION_TIMEOUT                         = 10                  /* [RFC4740] */
const val SIP_SA_DEREGISTRATION_TOO_MUCH_DATA                   = 11                  /* [RFC4740] */

/* SIP-Authentication-Scheme AVP Values (377) */
const val SIP_DIGEST                                            = 0                   /* [RFC4740] */

/* SIP-Reason-Code AVP Values (384) */
const val SIP_PERMANENT_TERMINATION                             = 0                   /* [RFC4740] */
const val SIP_NEW_SIP_SERVER_ASSIGNED                           = 1                   /* [RFC4740] */
const val SIP_SERVER_CHANGE                                     = 2                   /* [RFC4740] */
const val SIP_REMOVE_SIP_SERVER                                 = 3                   /* [RFC4740] */

/* SIP-User-Authorization-Type AVP Values (387) */
const val SIP_UA_REGISTRATION                                   = 0                   /* [RFC4740] */
const val SIP_UA_DEREGISTRATION                                 = 1                   /* [RFC4740] */
const val SIP_UA_REGISTRATION_AND_CAPABILITIES                  = 2                   /* [RFC4740] */

/* SIP-User-Data-Already-Available AVP Values (392) */
const val SIP_USER_DATA_NOT_AVAILABLE                           = 0                   /* [RFC4740] */
const val SIP_USER_DATA_ALREADY_AVAILABLE                       = 1                   /* [RFC4740] */

/* Accounting-Auth-Method AVP Values (code 406) */
const val AAM_PAP                                               = 1                   /* [RFC7155] */
const val AAM_CHAP                                              = 2                   /* [RFC7155] */
const val AAM_MS_CHAP_1                                         = 3                   /* [RFC7155] */
const val AAM_MS_CHAP_2                                         = 4                   /* [RFC7155] */
const val AAM_EAP                                               = 5                   /* [RFC7155] */
const val AAM_NONE                                              = 7                   /* [RFC7155] */

/* Origin-AAA-Protocol AVP Values (code 408) */
const val ORIGIN_AAA_PROTOCOL_RADIUS                            = 1                   /* [RFC7155] */

/* CC-Request-Type AVP Values (code 416) */
const val CC_INITIAL_REQUEST                                    = 1                   /* [RFC4006] */
const val CC_UPDATE_REQUEST                                     = 2                   /* [RFC4006] */
const val CC_TERMINATION_REQUEST                                = 3                   /* [RFC4006] */
const val CC_EVENT_REQUEST                                      = 4                   /* [RFC4006] */

/* CC-Session-Failover AVP Values (code 418) */
const val CC_FAILOVER_NOT_SUPPORTED                             = 0                   /* [RFC4006] */
const val CC_FAILOVER_SUPPORTED                                 = 1                   /* [RFC4006] */

/* Check-Balance-Result AVP Values (code 422) */
const val CHECK_BALANCE_ENOUGH_CREDIT                           = 0                   /* [RFC4006] */
const val CHECK_BALANCE_NO_CREDIT                               = 1                   /* [RFC4006] */

/* Credit-Control AVP Values (code 426) */
const val CC_CREDIT_AUTHORIZATION                               = 0                   /* [RFC4006] */
const val CC_RE_AUTHORIZATION                                   = 1                   /* [RFC4006] */
/* Credit-Control-Failure-Handling AVP Values (code 427) */
const val CC_TERMINATE                                          = 0                   /* [RFC4006] */
const val CC_CONTINUE                                           = 1                   /* [RFC4006] */
const val CC_RETRY_AND_TERMINATE                                = 2                   /* [RFC4006] */

/* Direct-Debiting-Failure-Handling AVP Values (code 428) */
const val DD_TERMINATE_OR_BUFFER                                = 0                   /* [RFC4006] */
const val DD_CONTINUE                                           = 1                   /* [RFC4006] */

/* Redirect-Address-Type AVP Values (code 433) */
const val RAT_IPV4_ADDRESS                                      = 0                   /* [RFC4006] */
const val RAT_IPV6_ADDRESS                                      = 1                   /* [RFC4006] */
const val RAT_URL                                               = 2                   /* [RFC4006] */
const val RAT_SIP_URI                                           = 3                   /* [RFC4006] */

/* Requested-Action AVP Values (436) */
const val RA_DIRECT_DEBITING                                    = 0                   /* [RFC4006] */
const val RA_REFUND_ACCOUNT                                     = 1                   /* [RFC4006] */
const val RA_CHECK_BALANCE                                      = 2                   /* [RFC4006] */
const val RA_PRICE_ENQUIRY                                      = 3                   /* [RFC4006] */

/* Final-Unit-Action AVP Values (code 449) */
const val FUA_TERMINATE                                         = 0                   /* [RFC4006] */
const val FUA_REDIRECT                                          = 1                   /* [RFC4006] */
const val FUA_RESTRICT_ACCESS                                   = 2                   /* [RFC4006] */

/* Subscription-Id-Type AVP Values (code 450) */
const val SIT_END_USER_EI64                                     = 0                   /* [RFC4006] */
const val SIT_END_USER_IMSI                                     = 1                   /* [RFC4006] */
const val SIT_END_USER_SIP_URI                                  = 2                   /* [RFC4006] */
const val SIT_END_USER_NAI                                      = 3                   /* [RFC4006] */
const val SIT_END_USER_PRIVATE                                  = 4                   /* [RFC4006] */

/* CC-Unit-Type AVP Values (code 454) */
const val CC_TIME                                               = 0                   /* [RFC4006] */
const val CC_MONEY                                              = 1                   /* [RFC4006] */
const val CC_TOTAL_OCTETS                                       = 2                   /* [RFC4006] */
const val CC_INPUT_OCTETS                                       = 3                   /* [RFC4006] */
const val CC_OUTPUT_OCTETS                                      = 4                   /* [RFC4006] */
const val CC_SERVICE_SPECIFIC_UNITS                             = 5                   /* [RFC4006] */

/* Multiple-Services-Indicator AVP Values (code 455) */
const val MULTIPLE_SERVICES_NOT_SUPPORTED                       = 0                   /* [RFC4006] */
const val MULTIPLE_SERVICES_SUPPORTED                           = 1                   /* [RFC4006] */

/* Tariff-Change-Usage AVP Values (code 452) */
const val TCU_UNIT_BEFORE_TARIFF_CHANGE                         = 0                   /* [RFC4006] */
const val TCU_UNIT_AFTER_TARIFF_CHANGE                          = 1                   /* [RFC4006] */
const val TCU_UNIT_INDETERMINATE                                = 2                   /* [RFC4006] */

/* User-Equipment-Info-Type AVP Values (code 459) */
const val UEIT_IMEISV                                           = 0                   /* [RFC4006] */
const val UEIT_MAC                                              = 1                   /* [RFC4006] */
const val UEIT_EUI64                                            = 2                   /* [RFC4006] */
const val UEIT_MODIFIED_EUI64                                   = 3                   /* [RFC4006] */

/* Accounting-Record-Type AVP Values (code 480) */
const val ACCT_EVENT_RECORD                                     = 1                   /* [RFC6733] */
const val ACCT_START_RECORD                                     = 2                   /* [RFC6733] */
const val ACCT_INTERIM_RECORD                                   = 3                   /* [RFC6733] */
const val ACCT_STOP_RECORD                                      = 4                   /* [RFC6733] */

/* Accounting-Realtime-Required AVP Values (code 483) */
const val ACCT_DELIVER_AND_GRANT                                = 1                   /* [RFC6733] */
const val ACCT_GRANT_AND_STORE                                  = 2                   /* [RFC6733] */
const val ACCT_GRANT_AND_LOSE                                   = 3                   /* [RFC6733] */

/* Treatment-Action AVP Values (code 572) */
const val TREATMENT_ACTION_DROP                                 = 0                   /* [RFC5777] */
const val TREATMENT_ACTION_SHAPE                                = 1                   /* [RFC5777] */
const val TREATMENT_ACTION_MARK                                 = 2                   /* [RFC5777] */
const val TREATMENT_ACTION_PERMIT                               = 3                   /* [RFC5777] */

/* QoS-Semantics AVP Values (code 575) */
const val QoS_DESIRED                                           = 0                   /* [RFC5777] */
const val QoS_AVAILABLE                                         = 1                   /* [RFC5777] */
const val QoS_DELIVERED                                         = 2                   /* [RFC5777] */
const val QoS_MINIMUM_QOS                                       = 3                   /* [RFC5777] */
const val QoS_AUTHORIZED                                        = 4                   /* [RFC5777] */

/* Key-Type AVP Values (code 582) */
const val DSRK                                                  = 0                   /* [RFC6734] */
const val RRK                                                   = 1                   /* [RFC6734] */
const val RMSK                                                  = 2                   /* [RFC6734] */
const val IKEv2_SK                                              = 3                   /* [RFC6738] */

/* NC-Request-Type AVP Values (code 595) */
const val NC_INITIAL_REQUEST                                    = 1                   /* [RFC6736] */
const val NC_UPDATE_REQUEST                                     = 2                   /* [RFC6736] */
const val NC_QUERY_REQUEST                                      = 3                   /* [RFC6736] */

/* NAT-External-Port-Style AVP Values (code 604) */
const val NAT_FOLLOW_INTERNAL_PORT_STYLE                        = 1                   /* [RFC6736] */
/* NAT-Control-Binding-Status (code 606) */
const val NAT_INITIAL_REQUEST                                   = 1                   /* [RFC6736] */
const val NAT_UPDATE_REQUEST                                    = 2                   /* [RFC6736] */
const val NAT_QUERY_REQUEST                                     = 3                   /* [RFC6736] */

/* OC-Feature-Vector AVP Values (code 622) */
const val OC_OLR_DEFAULT_ALGO                                   = 0x0000000000000001  /* [RFC7683] */

/* OC-Report-Type AVP Values (code 626) */
const val OC_HOST_REPORT                                        = 0                   /* [RFC7683] */

const val OC_REALM_REPORT                                       = 1                   /* [RFC7683] */

/* Known Vendor IDs */
const val VENDOR_ID_NONE                                        = 0L
const val VENDOR_ID_HP                                          = 11L                 /* Hewlett Packard */
const val VENDOR_ID_SUN                                         = 42L                 /* Sun Microsystems, Inc. */
const val VENDOR_ID_MERIT                                       = 61L                 /* Merit Networks */
const val VENDOR_ID_NOKIA                                       = 94L                 /* Nokia */
const val VENDOR_ID_ERICSSON                                    = 193L                /* Ericsson */
const val VENDOR_ID_USR                                         = 429L                /* US Robotics Corp. */
const val VENDOR_ID_ALU                                         = 637L                /* ALU Network */
const val VENDOR_ID_LUCENT                                      = 1751L               /* Lucent Technologies */
const val VENDOR_ID_HUAWEI                                      = 2011L               /* Huawei */
const val VENDOR_ID_3GPP2                                       = 5535L               /* 3GPP2 */
const val VENDOR_ID_3GPP                                        = 10415L              /* 3GPP */
const val VENDOR_ID_VODAFONE                                    = 12645L              /* Vodafone */
const val VENDOR_ID_ETSI                                        = 13019L              /* ETSI */

/* Known Application IDs */
const val APP_ID_DIAMETER_COMMON_MESSAGE                        = 0L                  /* [RFC6733] */
const val APP_ID_NASREQ                                         = 1L                  /* [RFC7155] */
const val APP_ID_MOBILE_IPv4                                    = 2L                  /* [RFC4004] */
const val APP_ID_DIAMETER_BASE_ACCOUNTING                       = 3L                  /* [RFC6733] */
const val APP_ID_DIAMETER_CREDIT_CONTROL                        = 4L                  /* [RFC4006] */
const val APP_ID_DIAMETER_EAP                                   = 5L                  /* [RFC4072] */
const val APP_ID_DAMETER_SIP_APPLICATION                        = 6L                  /* [RFC4740] */
const val APP_ID_DIAMETER_MOBILE_IPv6_IKE                       = 7L                  /* [RFC5778] */
const val APP_ID_DIAMETER_MOBILE_IPv6_AUTH                      = 8L                  /* [RFC5778] */
const val APP_ID_DIAMETER_QOS_APPLICATION                       = 9L                  /* [RFC5866] */
const val APP_ID_DIAMETER_CAPABILITIES_UPDATE                   = 10L                 /* [RFC6737] */
const val APP_ID_DIAMETER_IKE_SK                                = 11L                 /* [RFC6738] */
const val APP_ID_DIAMETER_NAT_CONTROL_APPLICATION               = 12L                 /* [RFC6736] */
const val APP_ID_DIAMETER_ERP                                   = 13L                 /* [RFC6942] */

/* Diameter's Expanding Applications */
const val APP_ID_3GPP_Cx                                        = 16777216L
const val APP_ID_3GPP_Sh                                        = 16777217L
const val APP_ID_3GPP_Re                                        = 16777218L
const val APP_ID_3GPP_Wx                                        = 16777219L
const val APP_ID_3GPP_Zn                                        = 16777220L
const val APP_ID_3GPP_Zh                                        = 16777221L
const val APP_ID_3GPP_Gq                                        = 16777222L
const val APP_ID_3GPP_Gmb                                       = 16777223L
const val APP_ID_3GPP_Gx                                        = 16777224L
const val APP_ID_3GPP_Gx_OVER_Gy                                = 16777225L
const val APP_ID_3GPP_MM10                                      = 16777226L
const val APP_ID_3GPP_Sta                                       = 16777250L
const val APP_ID_3GPP_S6a                                       = 16777251L
const val APP_ID_3GPP_S6d                                       = 16777251L
const val APP_ID_3GPP_S13                                       = 16777252L
const val APP_ID_3GPP_SLg                                       = 16777255L
const val APP_ID_3GPP_S6t                                       = 16777345L
const val APP_ID_3GPP_SWm                                       = 16777264L
const val APP_ID_3GPP_SWx                                       = 16777265L
const val APP_ID_3GPP_Gxx                                       = 16777266L
const val APP_ID_3GPP_S9                                        = 16777267L
const val APP_ID_3GPP_Zpn                                       = 16777268L
const val APP_ID_3GPP_S6b                                       = 16777272L
const val APP_ID_3GPP_SLh                                       = 16777291L
const val APP_ID_3GPP_SGmb                                      = 16777292L
const val APP_ID_3GPP_Sy                                        = 16777302L
const val APP_ID_3GPP_Sd                                        = 16777303L


fun createHopByHopId(): Long = System.currentTimeMillis() % 1000_000L

fun createEndToEndId(): Long {
  var e2eId = 0L
  val time = System.currentTimeMillis()

  val seed = ThreadLocalRandom.current().nextLong()
  e2eId = e2eId or ((seed and 0xFF) shl 24)
  e2eId = e2eId or (time and 0xFFFFFF)
  return e2eId
}

val EMPTY_BYTES = ByteArray(0)
const val ZERO_BYTE = 0.toByte()
val EMPTY_SHOTS = ShortArray(0)
const val ZERO_SHORT = 0.toShort()
val EMPTY_INTS = IntArray(0)
const val ZERO_INT = 0
val EMPTY_LONGS = LongArray(0)
const val ZERO_LONG = 0L
val EMPTY_STRING = arrayOfNulls<String>(0)


/**
 * Write 1 byte unsigned byte data to byte buffer.
 * @param buffer buffer
 * @param data byte
 */
fun writeUint8(buffer: ByteBuffer, data: Byte) {
  buffer.put(data)
}

/**
 * Write 2 bytes unsigned short data to byte buffer.
 * @param buffer buffer
 * @param data short
 */
fun writeUint16(buffer: ByteBuffer, data: Short) {
  buffer.put(((data and 0xFF00.toShort()).toInt() ushr 8).toByte())
  buffer.put((data and 0xFF).toByte())
}

/**
 * Write 3 bytes unsigned integer data to byte buffer.
 * @param buffer buffer
 * @param data integer
 */
fun writeUint24(buffer: ByteBuffer, data: Int) {
  buffer.put(((data and 0xFF0000) ushr 16).toByte())
  buffer.put(((data and 0xFF00) ushr 8).toByte())
  buffer.put((data and 0xFF).toByte())
}

/**
 * Write 4 bytes unsigned long data to byte buffer.
 * @param buffer buffer
 * @param data long
 */
fun writeUint32(buffer: ByteBuffer, data: Long) {
  buffer.put(((data and 0xFF000000L) ushr 24).toByte())
  buffer.put(((data and 0xFF0000) ushr 16).toByte())
  buffer.put(((data and 0xFF00) ushr 8).toByte())
  buffer.put((data and 0xFF).toByte())
}

/**
 * Read 1 byte unsigned short data from byte buffer.
 * @param buffer buffer
 * @return short
 */
fun readUint8(buffer: ByteBuffer, offset: Int = buffer.position()): Byte =
  buffer[offset].apply { buffer.position(buffer.position() + 1) }

/**
 * Read 2 bytes unsigned int data from byte buffer.
 * @param buffer buffer
 * @return int
 */
fun readUint16(buffer: ByteBuffer, offset: Int = buffer.position()): Short =
  (((buffer[offset] and 0xFF.toByte()).toInt() shl 8).toShort()
    or (buffer[offset] and 0xFF.toByte()).toShort())

/**
 * Read 3 bytes unsigned int data from byte buffer.
 * @param buffer buffer
 * @return int
 */
fun readUint24(buffer: ByteBuffer, offset: Int = buffer.position()): Int =
  (((buffer[offset] and 0xFF.toByte()).toInt() shl 16)
    or ((buffer[offset] and 0xFF.toByte()).toInt() shl 8)
    or (buffer[offset] and 0xFF.toByte()).toInt()).apply {
      buffer.position(buffer.position() + 3)
  }


/**
 * Read 3 bytes unsigned int data from byte array.
 * @param buffer bytes
 * @return int
 */
fun readUint24(buffer: ByteArray, offset: Int): Int =
  (((buffer[offset].toInt() and 0xFF) shl 16)
    or ((buffer[offset + 1].toInt() and 0xFF) shl 8)
    or (buffer[offset + 2].toInt() and 0xFF))


/**
 * Read 4 bytes unsigned long data from byte buffer.
 * @param buffer buffer
 * @return long
 */
fun readUint32(buffer: ByteBuffer, offset: Int = buffer.position()): Long =
  (((buffer[offset] and 0xFF.toByte()).toLong() shl 16)
    or ((buffer[offset] and 0xFF.toByte()).toLong() shl 16)
    or ((buffer[offset] and 0xFF.toByte()).toLong() shl 8)
    or (buffer[offset] and 0xFF.toByte()).toLong()).apply {
    buffer.position(buffer.position() + 4)
  }

/**
 * Calculate AVP data paddings.
 * @return int
 */
fun calculatePadding(length: Int): Int = if ((length and 3) != 0) (4 - (length and 3)) else 0

/**
 * Convert hexadecimal String into decoded byte array.
 *
 * @param hex hexadecimal string. e.g. "4130010A"
 * @return byte array with decoded hexadecimal input parameter.
 * @throws IllegalArgumentException if the parameter length is odd or contains non-hexadecimal characters.
 * @see byteArray2Hex
 */
@Throws(IllegalArgumentException::class)
fun hex2ByteArray(hex: String): ByteArray {
  var length = hex.length
  if ((1 and length) != 0) {
    throw IllegalArgumentException("'$hex' has odd length!")
  }
  length /= 2
  val result = ByteArray(length)

  var indexDest = 0
  var indexSrc = 0
  while(indexDest < length) {
    var digit = Character.digit(hex[indexSrc], 16)
    if (digit < 0) {
      throw IllegalArgumentException("'$hex' digit $indexSrc is not hexadecimal!")
    }
    result[indexDest] = (digit shl 4).toByte()
    ++indexSrc
    digit = Character.digit(hex[indexSrc], 16)
    if (digit < 0) {
      throw IllegalArgumentException("'$hex' digit $indexSrc is not hexadecimal!")
    }
    result[indexDest] = result[indexDest].or(digit.toByte())
    ++indexSrc
    ++indexDest
  }

  return result
}

/**
 * Byte array to hexadecimal string without separator.
 * @param byteArray byte array to be converted to string.
 * @return hexadecimal string, e.g "0142A3". `null`, if the provided byte array is `null`. "", if provided byte array is empty.
 * @see hex2ByteArray
 */
fun byteArray2Hex(byteArray: ByteArray?): String? =
  if (byteArray == null) null
  else if(byteArray.isEmpty()) ""
  else byteArray2HexString(byteArray)


private val BIN_TO_HEX_ARRAY = "0123456789ABCDEF".toCharArray()

/**
 * Byte array to hexadecimal display string.
 * @param byteArray byte array to be converted to string
 * @param sep separator. If `no_separator`, then no separator is used between the bytes
 * @param max maximum bytes to be converted. 0 to convert all bytes
 * @return hexadecimal string, e.g "01:45:A4", if ':' is used as separator. "--", if byte array is `null` or empty.
 */
fun byteArray2HexString(byteArray: ByteArray?, sep: Char = 0.toChar(), max: Int = 0): String {
  return if (byteArray != null && byteArray.isNotEmpty()) {
    var maxRef = max
    if (maxRef == 0 || maxRef > byteArray.size) {
      maxRef = byteArray.size
    }
    StringBuilder(maxRef *(if(sep == 0.toChar()) 2 else 3)).apply sb@{
      for(index in 0 until maxRef) {
        val value: Int = (byteArray[index] and 0xFF.toByte()).toInt()
        this@sb.append(BIN_TO_HEX_ARRAY[value ushr 4])
        this@sb.append(BIN_TO_HEX_ARRAY[value and 0x0F])
        if (sep != 0.toChar() && index < maxRef - 1) {
          this@sb.append(sep)
        }
      }
    }.toString()
  } else "--"
}

fun dottedIpToBytes(ipAddress: String): ByteArray {
  val str = StringTokenizer(ipAddress, ".")
  val addr = ByteArray(4)
  return ByteArray(4).apply addr@{
    for (idx in addr.indices) {
      this@addr[idx] = str.nextToken().toByte()
    }
  }
}

fun byteToDottedIp(ipAddress: ByteArray): String {
  return StringBuilder(StringBuilder().apply sb@{
    for (idx in 0 until 4) {
      val t = (0xFF.toByte() and ipAddress[idx]).toInt()
      this@sb.append('.').append(t)
    }
  }.substring(1)).toString()
}
