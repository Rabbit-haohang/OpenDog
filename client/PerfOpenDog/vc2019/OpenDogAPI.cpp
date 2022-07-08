//#include "OpenDogAPI.h"
//
//
//size_t receiveData(void* contents, size_t size, size_t nmemb, void* stream)
//{
//	std::string* str = (std::string*)stream;
//	(*str).append((char*)contents, size * nmemb);
//	return size * nmemb;
//}
//
//int loginAPI(string username, string password)
//{
//
//	string url = baseUrl + "user/login";
//	std::string response;
//
//	CURL* curl;
//	CURLcode res;
//	nlohmann::json body;
//	// ���������ַ
//	curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
//
//	// ��������ͷ��Ϣ
//	curl_slist* pList = nullptr;
//	pList = curl_slist_append(pList, "Content-Type:application/json");
//	curl_easy_setopt(curl, CURLOPT_HTTPHEADER, pList);
//	//�����ʷ�0��ʾ���β���Ϊpost
//	curl_easy_setopt(curl, CURLOPT_POST, 1);
//	//����������
//	body["username"] = username.c_str();
//	body["password"] = password.c_str();
//	curl_easy_setopt(curl, CURLOPT_POSTFIELDS, body.dump().c_str());
//	// ���ý��պ���
//	curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, receiveData);
//	// ���ý�������
//	curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void*)&response);
//
//	// ִ�в���鷵��ֵ
//	res = curl_easy_perform(curl);
//	if (res != CURLE_OK)
//	{
//		switch (res)
//		{
//		case CURLE_UNSUPPORTED_PROTOCOL:
//			fprintf(stderr, "��֧�ֵ�Э��,��URL��ͷ��ָ��\n");
//		case CURLE_COULDNT_CONNECT:
//			fprintf(stderr, "�������ӵ�remote�������ߴ���\n");
//		case CURLE_HTTP_RETURNED_ERROR:
//			fprintf(stderr, "http���ش���\n");
//		case CURLE_READ_ERROR:
//			fprintf(stderr, "�������ļ�����\n");
//		default:
//			fprintf(stderr, "����ֵ:%d\n", res);
//		}
//
//
//		return -1;
//	}
//	nlohmann::json j = nlohmann::json::parse(response);
//
//	std::cout << j["state"] << std::endl;
//
//	curl_easy_cleanup(curl);
//	curl_global_cleanup();
//	return 0;
//}
//
//
