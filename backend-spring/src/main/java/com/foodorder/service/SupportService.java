package com.foodorder.service;

import com.foodorder.dto.SupportCreateRequest;
import com.foodorder.dto.SupportStatusRequest;
import com.foodorder.model.SupportRequest;
import java.util.List;

public interface SupportService {
    List<SupportRequest> getAllSupportRequests();
    SupportRequest createSupportRequest(SupportCreateRequest request);
    SupportRequest updateSupportStatus(int supportRequestId, SupportStatusRequest request);
}
