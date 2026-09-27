package com.netzerohub.common.enums;

public enum ErrorCode {
    GenericError,

    InvalidCredentials,
    AccountNotFound,
    AccountAlreadyExists,
    AccountNotVerified,

    ResourceNotFound,
    ResourceAlreadyExists,
    ResourceConflict,
    ResourceNotOwnedByUser,

    InvalidInput,

}
