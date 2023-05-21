for n in {1..22}; do
    echo $n -  $(curl -s -w " :: HTTP %{http_code}, %{size_download} bytes, %{time_total} s" -X GET http://localhost:8080/books)
    sleep 0.5
done
