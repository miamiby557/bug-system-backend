package com.szcinda.express;

import com.szcinda.express.dto.MainDto;

public interface HomeService {
    MainDto query(String userId);
}
