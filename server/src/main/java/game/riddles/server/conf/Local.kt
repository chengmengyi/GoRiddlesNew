package game.riddles.server.conf

object Local {

    const val WEB="https://sites.google.com/view/go-riddles/home"
    const val EMAIL="En2424l53@gmail.com"


    const val OPEN="enterid_open"
    const val CONNECT="enterid_reward"
    const val HOME="enterid_home"
    const val RESULT="enterid_result"
    const val BACK="enterid_time"


    const val SERVER_LIST="""[
  {
    "goRi_pwd": "123456",
    "goRi_account": "chacha20-ietf-poly1305",
    "goRi_port": 100,
    "goRi_country": "Japan",
    "goRi_city": "Tokyo",
    "goRi_ip": "100.223.52.0"
  },
   {
    "goRi_pwd": "123456",
    "goRi_account": "chacha20-ietf-poly1305",
    "goRi_port": 100,
    "goRi_country": "United States",
    "goRi_city": "newyork",
    "goRi_ip": "100.223.52.1"
  }
]"""


    const val AD_LIST="""{
            "max_click":15,
    "max_show":50,
    "enterid_open": [
        {
            "from": "admob",
            "data_id": "ca-app-pub-3940256099942544/3419835294",
            "type": "o",
            "index": 1
        },
        {
            "from": "admob",
            "data_id": "ca-app-pub-3940256099942544/3419835294A",
            "type": "o",
            "index": 2
        }
    ],
     "enterid_home": [
        {
            "from": "admob",
            "data_id": "ca-app-pub-3940256099942544/2247696110",
            "type": "n",
            "index": 2
        }
    ],
      "enterid_result": [
        {
            "from": "admob",
            "data_id": "ca-app-pub-3940256099942544/2247696110",
            "type": "n",
            "index": 2
        }
    ],
     "enterid_reward": [
        {
            "from": "admob",
            "data_id": "ca-app-pub-3940256099942544/8691691433",
            "type": "i",
            "index": 2
        }
    ],
     "enterid_time": [
        {
            "from": "admob",
            "data_id": "ca-app-pub-3940256099942544/8691691433",
            "type": "i",
            "index": 2
        }
    ]
}"""
}