using Microsoft.VisualStudio.TestTools.UnitTesting;
using SSE.Business.Implementations;
using FluentAssertions;
using System;

namespace UnitTestProject2
{
    [TestClass]
    public class UnitTest1
    {
        [TestMethod]
        public void TestMethod1()
        {
            CryptoHelper cryptoHelper = new CryptoHelper();
            // Define a byte array.
            byte[] bytes = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20 };
            // Convert the array to a base 64 sring.
            String inputValue = Convert.ToBase64String(bytes);

            var result = cryptoHelper.GetDecryptedValue(inputValue);

            result.Should().NotBeNull();
        }
    }
}
