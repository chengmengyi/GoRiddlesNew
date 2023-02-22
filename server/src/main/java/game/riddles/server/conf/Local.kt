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
    "goRi_pwd": "9OuPVw#UBajyyzM",
    "goRi_account": "chacha20-ietf-poly1305",
    "goRi_port": 5691,
    "goRi_country": "Austria",
    "goRi_city": "Vienna1",
    "goRi_ip": "158.255.212.246"
  }
]"""


    const val AD_LIST="""{
    "max_click":15,
    "max_show":50,
    "enterid_open": [
        {
            "from": "admob",
            "data_id": "ca-app-pub-6337191878285963/1280294363",
            "type": "o",
            "index": 1
        }
    ],
     "enterid_home": [
        {
            "from": "admob",
            "data_id": "ca-app-pub-6337191878285963/7548329678",
            "type": "n",
            "index": 2
        }
    ],
      "enterid_result": [
        {
            "from": "admob",
            "data_id": "ca-app-pub-6337191878285963/5201367935",
            "type": "n",
            "index": 2
        }
    ],
     "enterid_reward": [
        {
            "from": "admob",
            "data_id": "ca-app-pub-6337191878285963/5305309712",
            "type": "i",
            "index": 2
        }
    ],
     "enterid_time": [
        {
            "from": "admob",
            "data_id": "ca-app-pub-6337191878285963/6426819699",
            "type": "i",
            "index": 2
        }
    ]
}"""
}