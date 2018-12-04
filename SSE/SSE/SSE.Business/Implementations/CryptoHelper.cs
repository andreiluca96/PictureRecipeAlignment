using System;
using System.Text;
using System.Security.Cryptography;
using System.IO;

namespace SSE.Business.Implementations
{
    public class CryptoHelper

    {
        //private readonly string IV = "SuFjcEmp/TE=";

        private readonly string IV = string.Empty;

        //private readonly string Key = "KIPSToILGp6fl+3gXJvMsN4IajizYBBT";

        private readonly string Key = string.Empty;
        /// <summary>

        /// Initializes a new instance of the <see cref="CryptoHelper"/> class.

        /// </summary>

        public CryptoHelper()

        {

            IV = "SuFjcEmp/TE=";

            Key = "KIPSToILGp6fl+3gXJvMsN4IajizYBBT";

        }

        /// <summary>

        /// Gets the encrypted value.

        /// </summary>

        /// <param name="inputValue">The input value.</param>

        /// <returns></returns>

        public string GetEncryptedValue(string inputValue)

        {

            TripleDESCryptoServiceProvider provider = this.GetCryptoProvider();

            // Create a MemoryStream.

            MemoryStream mStream = new MemoryStream();


            // Create a CryptoStream using the MemoryStream

            // and the passed key and initialization vector (IV).

            CryptoStream cStream = new CryptoStream(mStream, provider.CreateEncryptor(), CryptoStreamMode.Write);


            // Convert the passed string to a byte array.: Bug fixed, see update below!

            // byte[] toEncrypt = new ASCIIEncoding().GetBytes(inputValue);
            byte[] toEncrypt = new UTF8Encoding().GetBytes(inputValue);


            // Write the byte array to the crypto stream and flush it.

            cStream.Write(toEncrypt, 0, toEncrypt.Length);

            cStream.FlushFinalBlock();


            // Get an array of bytes from the

            // MemoryStream that holds the

            // encrypted data.

            byte[] ret = mStream.ToArray();


            // Close the streams.

            cStream.Close();

            mStream.Close();


            // Return the encrypted buffer.

            return Convert.ToBase64String(ret);

        }


        /// <summary>

        /// Gets the crypto provider.

        /// </summary>

        /// <returns></returns>

        private TripleDESCryptoServiceProvider GetCryptoProvider()

        {

            TripleDESCryptoServiceProvider provider = new TripleDESCryptoServiceProvider();

            provider.IV = Convert.FromBase64String(IV);

            provider.Key = Convert.FromBase64String(Key);

            return provider;

        }


        /// <summary>

        /// Gets the decrypted value.

        /// </summary>

        /// <param name="inputValue">The input value.</param>

        /// <returns></returns>

        public string GetDecryptedValue(string inputValue)

        {

            TripleDESCryptoServiceProvider provider = this.GetCryptoProvider();

            byte[] inputEquivalent = Convert.FromBase64String(inputValue);

            // Create a new MemoryStream.

            MemoryStream msDecrypt = new MemoryStream();


            // Create a CryptoStream using the MemoryStream

            // and the passed key and initialization vector (IV).

            CryptoStream csDecrypt = new CryptoStream(msDecrypt,

                provider.CreateDecryptor(),

                CryptoStreamMode.Write);

            csDecrypt.Write(inputEquivalent, 0, inputEquivalent.Length);

            csDecrypt.FlushFinalBlock();


            csDecrypt.Close();


            //Convert the buffer into a string and return it.

            return new UTF8Encoding().GetString(msDecrypt.ToArray());
        }

    }
}
